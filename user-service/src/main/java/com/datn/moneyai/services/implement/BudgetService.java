package com.datn.moneyai.services.implement;

import com.datn.moneyai.exceptions.UserMessageException;
import com.datn.moneyai.models.dtos.budget.BudgetRequest;
import com.datn.moneyai.models.dtos.budget.BudgetResponse;
import com.datn.moneyai.models.entities.bases.Budget;
import com.datn.moneyai.models.entities.bases.CategoryEntity;
import com.datn.moneyai.models.entities.bases.User;
import com.datn.moneyai.repositories.BudgetRepository;
import com.datn.moneyai.repositories.CategoryRepository;
import com.datn.moneyai.repositories.UserRepository;
import com.datn.moneyai.services.interfaces.IBudgetService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetService implements IBudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public BudgetResponse createBudget(BudgetRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserMessageException("Không tìm thấy người dùng."));

        if (request.getCategoryId() == null) {
            throw new UserMessageException("Thiếu danh mục.");
        }
        if (request.getMonth() == null || request.getYear() == null) {
            throw new UserMessageException("Thiếu tháng/năm.");
        }

        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new UserMessageException("Không tìm thấy danh mục."));
        if (!category.getUser().getId().equals(user.getId())) {
            throw new UserMessageException("Bạn không có quyền đặt ngân sách cho danh mục này.");
        }

        budgetRepository.findByUserAndCategoryAndMonthAndYear(user.getId(), category.getId(),
                request.getMonth(), request.getYear())
                .ifPresent(b -> {
                    throw new UserMessageException("Ngân sách cho danh mục đã tồn tại trong tháng/năm này.");
                });

        Budget budget = Budget.builder()
                .user(user)
                .category(category)
                .limitAmount(request.getLimitAmount())
                .month(request.getMonth())
                .year(request.getYear())
                .build();
        Budget saved = budgetRepository.save(budget);
        return toResponse(saved);
    }

    @Override
    public BudgetResponse updateBudget(Long id, BudgetRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserMessageException("Không tìm thấy người dùng."));

        Budget budget = budgetRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new UserMessageException("Không tìm thấy ngân sách."));

        if (request.getLimitAmount() != null) {
            budget.setLimitAmount(request.getLimitAmount());
        }
        if (request.getMonth() != null) {
            budget.setMonth(request.getMonth());
        }
        if (request.getYear() != null) {
            budget.setYear(request.getYear());
        }
        if (request.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new UserMessageException("Không tìm thấy danh mục."));
            if (!category.getUser().getId().equals(user.getId())) {
                throw new UserMessageException("Bạn không có quyền gán danh mục này.");
            }
            budget.setCategory(category);
        }

        budgetRepository.findByUserAndCategoryAndMonthAndYear(user.getId(), budget.getCategory().getId(),
                budget.getMonth(), budget.getYear())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(budget.getId())) {
                        throw new UserMessageException("Đã tồn tại ngân sách trùng danh mục/tháng/năm.");
                    }
                });

        Budget saved = budgetRepository.save(budget);
        return toResponse(saved);
    }

    @Override
    public BudgetResponse getBudget(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserMessageException("Không tìm thấy người dùng."));

        Budget budget = budgetRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new UserMessageException("Không tìm thấy ngân sách."));
        return toResponse(budget);
    }

    @Override
    public BudgetResponse getBudgetByCategory(Long categoryId, Integer month, Integer year) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserMessageException("Không tìm thấy người dùng."));

        if (categoryId == null) {
            throw new UserMessageException("Thiếu danh mục.");
        }
        LocalDate now = LocalDate.now();
        int m = month != null ? month : now.getMonthValue();
        int y = year != null ? year : now.getYear();

        Budget budget = budgetRepository.findByUserAndCategoryAndMonthAndYear(user.getId(), categoryId, m, y)
                .orElseThrow(() -> new UserMessageException("Không tìm thấy ngân sách theo danh mục."));
        return toResponse(budget);
    }

    @Override
    public List<BudgetResponse> listBudgets(Integer month, Integer year) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserMessageException("Không tìm thấy người dùng."));

        LocalDate now = LocalDate.now();
        int m = month != null ? month : now.getMonthValue();
        int y = year != null ? year : now.getYear();

        return budgetRepository.findAllByUserAndMonthAndYear(user.getId(), m, y)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteBudget(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserMessageException("Không tìm thấy người dùng."));

        Budget budget = budgetRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new UserMessageException("Không tìm thấy ngân sách."));
        budgetRepository.delete(budget);
    }

    private BudgetResponse toResponse(Budget budget) {
        BudgetResponse res = new BudgetResponse();
        res.setId(budget.getId());
        res.setLimitAmount(budget.getLimitAmount());
        res.setMonth(budget.getMonth());
        res.setYear(budget.getYear());
        res.setCategoryId(budget.getCategory() != null ? budget.getCategory().getId() : null);
        res.setCategoryName(budget.getCategory() != null ? budget.getCategory().getName() : null);
        res.setCreatedAt(budget.getCreatedAt());
        res.setUpdatedAt(budget.getUpdatedAt());
        return res;
        }
}
