package com.datn.moneyai.services.implement;

import com.datn.moneyai.exceptions.UserMessageException;
import com.datn.moneyai.models.dtos.saving_transaction.SavingGoalDetailResponse;
import com.datn.moneyai.models.dtos.saving_transaction.SavingTransactionRequest;
import com.datn.moneyai.models.dtos.saving_transaction.SavingTransactionResponse;
import com.datn.moneyai.models.entities.bases.SavingGoalEntity;
import com.datn.moneyai.models.entities.bases.SavingTransactionEntity;
import com.datn.moneyai.models.entities.bases.UserEntity;
import com.datn.moneyai.models.entities.enums.SavingGoalStatus;
import com.datn.moneyai.models.entities.enums.SavingTransactionType;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.repositories.SavingGoalRepository;
import com.datn.moneyai.repositories.SavingTransactionRepository;
import com.datn.moneyai.repositories.UserRepository;
import com.datn.moneyai.services.interfaces.ISavingTransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavingTransactionService implements ISavingTransactionService {

    @Autowired
    private SavingTransactionRepository savingTransactionRepository;

    @Autowired
    private SavingGoalRepository savingGoalRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserMessageException("Không tìm thấy người dùng"));
    }

    private SavingTransactionResponse mapToResponse(SavingTransactionEntity savingTransactionEntity) {
        return SavingTransactionResponse.builder()
                .savingGoalId(savingTransactionEntity.getSavingGoalEntity().getId())
                .amount(savingTransactionEntity.getAmount())
                .currentAmount(savingTransactionEntity.getSavingGoalEntity().getCurrentAmount() != null
                        ? savingTransactionEntity.getSavingGoalEntity().getCurrentAmount()
                        : BigDecimal.ZERO)
                .transactionDate(savingTransactionEntity.getTransactionDate())
                .type(savingTransactionEntity.getType())
                .note(savingTransactionEntity.getNote())
                .build();
    }

    @Override
    @Transactional
    public ApiResult<SavingTransactionResponse> createSavingTransaction(SavingTransactionRequest request) {
        UserEntity currentUser = getCurrentUser();

        if (request == null) {
            throw new UserMessageException("Dữ liệu yêu cầu không hợp lệ");
        }

        if (request.getAmount() == null || request.getAmount().signum() <= 0) {
            throw new UserMessageException("Số tiền giao dịch phải lớn hơn 0");
        }

        if (request.getType() == null) {
            throw new UserMessageException("Loại giao dịch (Nạp/Rút) không được để trống");
        }

        SavingGoalEntity savingGoal = savingGoalRepository.findById(request.getSavingGoalId())
                .orElseThrow(() -> new UserMessageException("Không tìm thấy mục tiêu tiết kiệm"));

        if (!savingGoal.getUser().getId().equals(currentUser.getId())) {
            throw new UserMessageException("Bạn không có quyền thực hiện giao dịch cho mục tiêu này");
        }

        // 1. LẤY SỐ DƯ HIỆN TẠI TRỰC TIẾP TỪ ENTITY (Cực nhanh, không cần query SUM)
        BigDecimal currentBalance = savingGoal.getCurrentAmount();
        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
        }

        // 2. Kiểm tra logic Rút tiền
        if (request.getType() == SavingTransactionType.WITHDRAWAL) {
            if (currentBalance.compareTo(request.getAmount()) < 0) {
                throw new UserMessageException(
                        "Số dư trong quỹ không đủ để rút. Tối đa chỉ có thể rút: " + currentBalance);
            }
        }

        // 3. TÍNH TOÁN SỐ DƯ MỚI
        BigDecimal newBalance = request.getType() == SavingTransactionType.DEPOSIT
                ? currentBalance.add(request.getAmount())
                : currentBalance.subtract(request.getAmount());

        // 4. CẬP NHẬT LẠI SỐ DƯ VÀ TRẠNG THÁI CHO GOAL
        savingGoal.setCurrentAmount(newBalance);
        if (newBalance.compareTo(savingGoal.getTargetAmount()) >= 0) {
            savingGoal.setStatus(SavingGoalStatus.COMPLETED);
        } else {
            savingGoal.setStatus(SavingGoalStatus.ONGOING);
        }

        // 5. Tạo và lưu Transaction
        SavingTransactionEntity savingTransactionEntity = SavingTransactionEntity.builder()
                .savingGoalEntity(savingGoal)
                .amount(request.getAmount())
                .transactionDate(request.getTransactionDate() != null ? request.getTransactionDate() : LocalDate.now())
                .type(request.getType())
                .note(request.getNote())
                .build();

        // 6. Lưu cả 2 vào Database
        savingGoalRepository.save(savingGoal);
        SavingTransactionEntity savedEntity = savingTransactionRepository.save(savingTransactionEntity);

        return ApiResult.success(mapToResponse(savedEntity), "Tạo mới giao dịch quỹ thành công");
    }

    @Override
    public ApiResult<SavingGoalDetailResponse> getSavingTransaction(Long savingGoalId) {

        // 1. Tìm Goal để lấy currentAmount (số dư hiện tại)
        SavingGoalEntity savingGoal = savingGoalRepository.findById(savingGoalId)
                .orElseThrow(() -> new UserMessageException("Không tìm thấy mục tiêu tiết kiệm"));

        // 2. Lấy danh sách giao dịch
        List<SavingTransactionEntity> savingTransactionEntities = savingTransactionRepository
                .findBySavingGoalId(savingGoalId);

        List<SavingTransactionResponse> savingTransactionResponses = savingTransactionEntities.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        // 3. Đóng gói cả số dư và danh sách giao dịch vào DTO mới
        SavingGoalDetailResponse responseData = SavingGoalDetailResponse.builder()
                .currentAmount(savingGoal.getCurrentAmount() != null ? savingGoal.getCurrentAmount() : BigDecimal.ZERO)
                .transactions(savingTransactionResponses)
                .build();

        return ApiResult.success(responseData, "Lấy chi tiết giao dịch mục tiêu tiết kiệm thành công");
    }
}
