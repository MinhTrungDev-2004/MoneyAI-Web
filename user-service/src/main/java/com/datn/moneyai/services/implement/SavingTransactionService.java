package com.datn.moneyai.services.implement;

import com.datn.moneyai.exceptions.UserMessageException;
import com.datn.moneyai.models.dtos.SavingTransaction.SavingTransactionRequest;
import com.datn.moneyai.models.dtos.SavingTransaction.SavingTransactionResponse;
import com.datn.moneyai.models.entities.bases.SavingGoalEntity;
import com.datn.moneyai.models.entities.bases.SavingTransactionEntity;
import com.datn.moneyai.models.entities.bases.UserEntity;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.repositories.SavingGoalRepository;
import com.datn.moneyai.repositories.SavingTransactionRepository;
import com.datn.moneyai.repositories.UserRepository;
import com.datn.moneyai.services.interfaces.ISavingTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;

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

    private SavingTransactionResponse mapToResponse(SavingTransactionEntity entity) {
        return SavingTransactionResponse.builder()
                .savingGoalId(entity.getSavingGoalEntity().getId())
                .amount(entity.getAmount())
                .transactionDate(entity.getTransactionDate())
                .note(entity.getNote())
                .build();
    }

    @Override
    public ApiResult<SavingTransactionResponse> createSavingTransaction(SavingTransactionRequest request) {
        UserEntity currentUser = getCurrentUser();

        if (request == null) {
            throw new UserMessageException("Dữ liệu yêu cầu không hợp lệ");
        }

        if (request.getAmount() == null || request.getAmount().signum() <= 0) {
            throw new UserMessageException("Số tiền phải lớn hơn 0");
        }

        SavingGoalEntity savingGoal = savingGoalRepository.findById(request.getSavingGoalId())
                .orElseThrow(() -> new UserMessageException("Không tìm thấy mục tiêu tiết kiệm"));

        if (!savingGoal.getUser().getId().equals(currentUser.getId())) {
            throw new UserMessageException("Bạn không có quyền thực hiện giao dịch cho mục tiêu này");
        }

        SavingTransactionEntity savingTransactionEntity = SavingTransactionEntity.builder()
                .savingGoalEntity(savingGoal)
                .amount(request.getAmount())
                .transactionDate(request.getTransactionDate() != null ? request.getTransactionDate() : LocalDate.now())
                .note(request.getNote())
                .build();

        SavingTransactionEntity savedEntity = savingTransactionRepository.save(savingTransactionEntity);
        
        return ApiResult.success(mapToResponse(savedEntity), "Tạo mới giao dịch tích lũy thành công");
    }
}
