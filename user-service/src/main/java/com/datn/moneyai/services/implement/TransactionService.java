package com.datn.moneyai.services.implement;

import com.datn.moneyai.models.entities.bases.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

import com.datn.moneyai.models.dtos.transaction.TransactionRequest;
import com.datn.moneyai.models.dtos.transaction.TransactionResponse;
import com.datn.moneyai.models.dtos.transaction.TransactionUpdateRequest;
import com.datn.moneyai.models.entities.bases.CategoryEntity;
import com.datn.moneyai.models.entities.bases.TransactionEntity;
import com.datn.moneyai.models.entities.enums.TransactionSource;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.repositories.CategoryRepository;
import com.datn.moneyai.repositories.TransactionRepository;
import com.datn.moneyai.repositories.UserRepository;
import com.datn.moneyai.services.interfaces.ITransactionService;
import com.datn.moneyai.exceptions.UserMessageException;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserMessageException("Không tìm thấy người dùng."));
    }

    private TransactionResponse mapToResponse(TransactionEntity transactionEntity) {
        return TransactionResponse.builder()
                .id(transactionEntity.getId())
                .categoryId(transactionEntity.getCategory().getId())
                .categoryName(transactionEntity.getCategory().getName())
                .iconCategory(transactionEntity.getCategory().getIcon())
                .colorCode(transactionEntity.getCategory().getColorCode())
                .categoryType(transactionEntity.getCategory().getType())
                .amount(transactionEntity.getTotalAmount())
                .transactionDate(transactionEntity.getTransactionDate())
                .note(transactionEntity.getNote())
                .build();
    }


    @Override
    public ApiResult<TransactionResponse> createTransaction(TransactionRequest request) {
        if (request == null) throw new UserMessageException("Dữ liệu yêu cầu không hợp lệ");
        if (request.getAmount() == null || request.getAmount().signum() <= 0) {
            throw new UserMessageException("Số tiền phải lớn hơn 0");
        }
        if (request.getTransactionDate() == null) throw new UserMessageException("Vui lòng chọn ngày giao dịch");
        if (request.getCategoryId() == null) throw new UserMessageException("Vui lòng chọn danh mục");

        UserEntity user = getCurrentUser();

        CategoryEntity category = categoryRepository.findActiveCategoryById(request.getCategoryId())
                .orElseThrow(() -> new UserMessageException("Không tìm thấy danh mục hoặc danh mục đã bị xóa!"));

        if (!Objects.equals(category.getUser().getId(), user.getId())) {
            throw new UserMessageException("Bạn không có quyền sử dụng danh mục này.");
        }

        TransactionEntity transaction = TransactionEntity.builder()
                .totalAmount(request.getAmount())
                .note(request.getNote())
                .transactionDate(request.getTransactionDate() != null ? request.getTransactionDate() : LocalDateTime.now().toLocalDate())
                .category(category)
                .user(user)
                .source(TransactionSource.MANUAL.name())
                .build();

        TransactionEntity savedTransaction = transactionRepository.save(transaction);

        return ApiResult.success(mapToResponse(savedTransaction), "Tạo giao dịch thành công");
    }

    @Override
    public ApiResult<TransactionResponse> updateTransaction(Long id, TransactionUpdateRequest request) {
        if (id == null) throw new UserMessageException("Thiếu id giao dịch");

        UserEntity user = getCurrentUser();

        TransactionEntity transactionEntity = transactionRepository.findActiveByIdAndUser(id, user.getId())
                .orElseThrow(() -> new UserMessageException("Không tìm thấy giao dịch hoặc đã bị xóa!"));

        if (request.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findActiveCategoryById(request.getCategoryId())
                    .orElseThrow(() -> new UserMessageException("Không tìm thấy danh mục hoặc danh mục đã bị xóa!"));

            if (!Objects.equals(category.getUser().getId(), user.getId())) {
                throw new UserMessageException("Bạn không có quyền sử dụng danh mục này.");
            }
            transactionEntity.setCategory(category);
        }

        if (request.getAmount() != null) {
            if (request.getAmount().signum() <= 0) throw new UserMessageException("Số tiền phải lớn hơn 0");
            transactionEntity.setTotalAmount(request.getAmount());
        }

        if (request.getTransactionDate() != null) {
            transactionEntity.setTransactionDate(request.getTransactionDate());
        }

        if (request.getNote() != null) {
            transactionEntity.setNote(request.getNote());
        }

        TransactionEntity saved = transactionRepository.save(transactionEntity);

        return ApiResult.success(mapToResponse(saved), "Cập nhật giao dịch thành công");
    }

    @Override
    public ApiResult<Void> deleteTransaction(Long id) {
        if (id == null) throw new UserMessageException("Thiếu id giao dịch");
        UserEntity user = getCurrentUser();
        TransactionEntity transactionEntity = transactionRepository.findActiveByIdAndUser(id, user.getId())
                .orElseThrow(() -> new UserMessageException("Không tìm thấy giao dịch hoặc đã bị xóa!"));
        transactionRepository.delete(transactionEntity);
        return ApiResult.success(null, "Xóa giao dịch thành công");
    }

    @Override
    public ApiResult<List<TransactionResponse>> getTransactionsByCategory(Long categoryId) {
        if (categoryId == null) throw new UserMessageException("Thiếu id danh mục");

        UserEntity user = getCurrentUser();

        List<TransactionEntity> list = transactionRepository.findAllActiveByCategoryAndUser(categoryId, user.getId());
        List<TransactionResponse> responseList = list.stream().map(this::mapToResponse).toList();

        return ApiResult.success(responseList, "Lấy danh sách giao dịch theo danh mục thành công");
    }

    @Override
    public ApiResult<List<TransactionResponse>> getTransactionsByMonth(YearMonth monthYear) {
        if (monthYear == null) throw new UserMessageException("Thiếu tháng cần tra cứu");

        UserEntity user = getCurrentUser();

        LocalDate startDate = monthYear.atDay(1);
        LocalDate endDate = monthYear.atEndOfMonth();

        List<TransactionEntity> list = transactionRepository.findAllByUserAndDateBetween(
                user.getId(), startDate, endDate);

        List<TransactionResponse> responseList = list.stream().map(this::mapToResponse).toList();

        return ApiResult.success(responseList, "Lấy danh sách giao dịch theo tháng thành công");
    }

    @Override
    public ApiResult<List<TransactionResponse>> getTransactionsByYear(Year year) {
        if (year == null) {
            throw new UserMessageException("Thiếu năm cần tra cứu");
        }
        UserEntity user = getCurrentUser();
        LocalDate startDate = year.atDay(1);
        LocalDate endDate = year.atMonth(12).atEndOfMonth();
        List<TransactionEntity> list = transactionRepository.findAllByUserAndDateBetween(
                user.getId(), startDate, endDate);

        List<TransactionResponse> responseList = list.stream().map(this::mapToResponse).toList();

        return ApiResult.success(responseList, "Lấy danh sách giao dịch năm " + year.getValue() + " thành công");
    }
}