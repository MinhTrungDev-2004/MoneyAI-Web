package com.datn.moneyai.services.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.datn.moneyai.models.dtos.transaction.TransactionRequest;
import com.datn.moneyai.models.dtos.transaction.TransactionResponse;
import com.datn.moneyai.models.dtos.transaction.TransactionUpdateRequest;
import com.datn.moneyai.models.entities.bases.CategoryEntity;
import com.datn.moneyai.models.entities.bases.TransactionEntity;
import com.datn.moneyai.models.entities.bases.User;
import com.datn.moneyai.models.entities.enums.TransactionSource;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.repositories.CategoryRepository;
import com.datn.moneyai.repositories.TransactionRepository;
import com.datn.moneyai.repositories.UserRepository;
import com.datn.moneyai.services.interfaces.ITransactionService;
import com.datn.moneyai.exceptions.UserMessageException;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserMessageException("Không tìm thấy người dùng."));
    }

    private TransactionResponse mapToResponse(TransactionEntity transactionEntity) {
        CategoryEntity categoryEntity = transactionEntity.getCategory();
        return TransactionResponse.builder()
                .id(transactionEntity.getId())
                .categoryId(categoryEntity != null ? categoryEntity.getId() : null)
                .categoryName(categoryEntity != null ? categoryEntity.getName() : null)
                .categoryType(categoryEntity != null ? categoryEntity.getType() : null)
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

        User user = getCurrentUser();

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

        User user = getCurrentUser();

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
        User user = getCurrentUser();
        TransactionEntity transactionEntity = transactionRepository.findActiveByIdAndUser(id, user.getId())
                .orElseThrow(() -> new UserMessageException("Không tìm thấy giao dịch hoặc đã bị xóa!"));
        transactionRepository.delete(transactionEntity);
        return ApiResult.success(null, "Xóa giao dịch thành công");
    }

    @Override
    public ApiResult<List<TransactionResponse>> getTransactionsByCategory(Long categoryId) {
        if (categoryId == null) throw new UserMessageException("Thiếu id danh mục");

        User user = getCurrentUser();

        List<TransactionEntity> list = transactionRepository.findAllActiveByCategoryAndUser(categoryId, user.getId());
        List<TransactionResponse> responseList = list.stream().map(this::mapToResponse).toList();

        return ApiResult.success(responseList, "Lấy danh sách giao dịch theo danh mục thành công");
    }

    @Override
    public ApiResult<List<TransactionResponse>> getTransactionsByDate(LocalDate date) {
        if (date == null) throw new UserMessageException("Thiếu ngày cần tra cứu");

        User user = getCurrentUser();

        List<TransactionEntity> list = transactionRepository.findAllByUserAndDate(user.getId(), date);
        List<TransactionResponse> responseList = list.stream().map(this::mapToResponse).toList();

        return ApiResult.success(responseList, "Lấy danh sách giao dịch theo ngày thành công");
    }

    @Override
    public ApiResult<BigDecimal> calculateTotalIncome() {
        User user = getCurrentUser();
        return ApiResult.success(transactionRepository.calculateTotalIncome(user.getId()), "Lấy tổng thu nhập thành công");
    }

    @Override
    public ApiResult<BigDecimal> calculateTotalExpense() {
        User user = getCurrentUser();
        return ApiResult.success(transactionRepository.calculateTotalExpense(user.getId()), "Lấy tổng chi tiêu thành công");
    }
}