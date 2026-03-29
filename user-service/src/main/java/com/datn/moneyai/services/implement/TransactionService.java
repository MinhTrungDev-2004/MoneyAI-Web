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
import com.datn.moneyai.repositories.CategoryRepository;
import com.datn.moneyai.repositories.TransactionRepository;
import com.datn.moneyai.repositories.UserRepository;
import com.datn.moneyai.services.interfaces.ITransactionService;
import com.datn.moneyai.exceptions.UserMessageException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

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
    public TransactionResponse createTransaction(TransactionRequest request) {
        if (request == null) throw new UserMessageException("Dữ liệu yêu cầu không hợp lệ");

        if (request.getAmount() == null || request.getAmount().signum() <= 0) {
            throw new UserMessageException("Số tiền phải lớn hơn 0");
        }

        if (request.getTransactionDate() == null) throw new UserMessageException("Vui lòng chọn ngày giao dịch");

        if (request.getCategoryId() == null) throw new UserMessageException("Vui lòng chọn danh mục");

        UserEntity user = getCurrentUser();

        CategoryEntity category = categoryRepository.findActiveCategoryById(request.getCategoryId())
                .orElseThrow(() -> new UserMessageException("Không tìm thấy danh mục hoặc danh mục đã bị xóa!"));

        TransactionEntity transaction = TransactionEntity.builder()
                .totalAmount(request.getAmount())
                .note(request.getNote())
                .transactionDate(request.getTransactionDate() != null ? request.getTransactionDate() : LocalDateTime.now().toLocalDate())
                .category(category)
                .user(user)
                .build();

        TransactionEntity savedTransaction = transactionRepository.save(transaction);

        return mapToResponse(savedTransaction);
    }

    @Override
    public TransactionResponse updateTransaction(Long id, TransactionUpdateRequest request) {
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

        return mapToResponse(saved);
    }

    @Override
    public void deleteTransaction(Long id) {
        if (id == null) throw new UserMessageException("Thiếu id giao dịch");
        UserEntity user = getCurrentUser();
        TransactionEntity transactionEntity = transactionRepository.findActiveByIdAndUser(id, user.getId())
                .orElseThrow(() -> new UserMessageException("Không tìm thấy giao dịch hoặc đã bị xóa!"));
        transactionRepository.delete(transactionEntity);
    }

    @Override
    public List<TransactionResponse> getTransactionsByCategory(Long categoryId) {
        if (categoryId == null) throw new UserMessageException("Thiếu id danh mục");

        UserEntity user = getCurrentUser();

        List<TransactionEntity> list = transactionRepository.findAllActiveByCategoryAndUser(categoryId, user.getId());
        List<TransactionResponse> responseList = list.stream().map(this::mapToResponse).toList();

        return responseList;
    }

    @Override
    public List<TransactionResponse> getTransactionsByMonth(YearMonth monthYear) {
        if (monthYear == null) throw new UserMessageException("Thiếu tháng cần tra cứu");

        UserEntity user = getCurrentUser();

        LocalDate startDate = monthYear.atDay(1);
        LocalDate endDate = monthYear.atEndOfMonth();

        List<TransactionEntity> list = transactionRepository.findAllByUserAndDateBetween(
                user.getId(), startDate, endDate);

        List<TransactionResponse> responseList = list.stream().map(this::mapToResponse).toList();

        return responseList;
    }

    @Override
    public List<TransactionResponse> getTransactionsByYear(Year year) {
        if (year == null) {
            throw new UserMessageException("Thiếu năm cần tra cứu");
        }
        UserEntity user = getCurrentUser();
        LocalDate startDate = year.atDay(1);
        LocalDate endDate = year.atMonth(12).atEndOfMonth();
        List<TransactionEntity> list = transactionRepository.findAllByUserAndDateBetween(
                user.getId(), startDate, endDate);

        List<TransactionResponse> responseList = list.stream().map(this::mapToResponse).toList();

        return responseList;
    }
}
