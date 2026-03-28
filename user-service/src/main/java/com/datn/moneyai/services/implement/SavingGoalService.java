package com.datn.moneyai.services.implement;

import com.datn.moneyai.exceptions.UserMessageException;
import com.datn.moneyai.models.dtos.saving.SavingGoalRequest;
import com.datn.moneyai.models.dtos.saving.SavingGoalResponse;
import com.datn.moneyai.models.entities.bases.SavingGoalEntity;
import com.datn.moneyai.models.entities.bases.UserEntity;
import com.datn.moneyai.models.entities.enums.SavingGoalStatus;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.repositories.SavingGoalRepository;
import com.datn.moneyai.repositories.UserRepository;
import com.datn.moneyai.services.interfaces.ISavingGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavingGoalService implements ISavingGoalService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SavingGoalRepository savingGoalRepository;

    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserMessageException("Người dùng không tồn tại"));
    }

    private SavingGoalResponse mapToGetResponse(SavingGoalEntity savingGoalEntity) {
        return SavingGoalResponse.builder()
                .id(savingGoalEntity.getId())
                .name(savingGoalEntity.getName())
                .targetAmount(savingGoalEntity.getTargetAmount())
                .currentAmount(savingGoalEntity.getCurrentAmount() != null ? savingGoalEntity.getCurrentAmount() : BigDecimal.ZERO)
                .deadlineDate(savingGoalEntity.getDeadlineDate())
                .status(savingGoalEntity.getStatus())
                .iconName(savingGoalEntity.getIconName())
                .colorClass(savingGoalEntity.getColorClass())
                .bgClass(savingGoalEntity.getBgClass())
                .updatedAt(savingGoalEntity.getUpdatedAt())
                .createdAt(savingGoalEntity.getCreatedAt())
                .build();
    }

    @Override
    public ApiResult<SavingGoalResponse> createSavingGoal(SavingGoalRequest request) {
        UserEntity user = getCurrentUser();

        SavingGoalEntity savingGoalEntity = SavingGoalEntity.builder()
                .user(user)
                .name(request.getName())
                .targetAmount(request.getTargetAmount())
                .currentAmount(request.getCurrentAmount())
                .deadlineDate(request.getDeadlineDate())
                .status(request.getStatus() != null ? request.getStatus() : SavingGoalStatus.ONGOING)
                .iconName(request.getIconName())
                .colorClass(request.getColorClass())
                .bgClass(request.getBgClass())
                .build();

        SavingGoalEntity savedGoalEntity = savingGoalRepository.save(savingGoalEntity);
        return ApiResult.success(mapToGetResponse(savedGoalEntity), "Tạo mới mục tiêu thành công");
    }

    @Override
    public ApiResult<SavingGoalResponse> updateSavingGoal(Long id, SavingGoalRequest request) {
        UserEntity user = getCurrentUser();

        SavingGoalEntity savingGoalEntity = savingGoalRepository.findById(id)
                .orElseThrow(() -> new UserMessageException("Mục tiêu không tồn tại"));

        if (!savingGoalEntity.getUser().getId().equals(user.getId())) {
            throw new UserMessageException("Bạn không có quyền cập nhật thông báo này");
        }

        if (request.getName() != null && !request.getName().isEmpty()) {
            savingGoalEntity.setName(request.getName());
        }

        if (request.getTargetAmount() != null) {
            savingGoalEntity.setTargetAmount(request.getTargetAmount());
        }

        if (request.getDeadlineDate() != null && request.getDeadlineDate().isBefore(LocalDate.now())) {
            throw new UserMessageException("Chọn ngày lớn hơn hiện tại");
        }

        if (request.getDeadlineDate() != null) {
            savingGoalEntity.setDeadlineDate(request.getDeadlineDate());
        }

        if (request.getIconName() != null) {
            savingGoalEntity.setIconName(request.getIconName());
        }

        if (request.getColorClass() != null) {
            savingGoalEntity.setColorClass(request.getColorClass());
        }

        if (request.getBgClass() != null) {
            savingGoalEntity.setBgClass(request.getBgClass());
        }

        SavingGoalEntity updateSavingGoal = savingGoalRepository.save(savingGoalEntity);
        return ApiResult.success(mapToGetResponse(updateSavingGoal), "Cập nhật thành công");
    }

    @Override
    public ApiResult<List<SavingGoalResponse>> getsSavingGoal() {
        UserEntity user = getCurrentUser();

        List<SavingGoalEntity> savingGoalEntities = savingGoalRepository.findByUser(user.getId());
        List<SavingGoalResponse> response = savingGoalEntities.stream()
                .map(this::mapToGetResponse)
                .collect(Collectors.toList());
        return ApiResult.success(response, "Lấy danh sách mục tiêu tích kiệm thành công");
    }

    @Override
    public ApiResult<SavingGoalResponse> deleteSavingGoal(Long id) {
        UserEntity user = getCurrentUser();
        SavingGoalEntity savingGoalEntity = savingGoalRepository.findByIdAndUserId(user.getId(), id)
                .orElseThrow(() -> new UserMessageException("Không tìm thấy mục tiêu tích kiệm hoặc đã bị xóa!"));
        savingGoalRepository.delete(savingGoalEntity);
        return ApiResult.success(null, "Xóa mục tiêu tích kiệm thành công");
    }
}
