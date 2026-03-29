package com.datn.moneyai.services.implement;

import com.datn.moneyai.exceptions.UserMessageException;
import com.datn.moneyai.models.dtos.budget.BudgetRequest;
import com.datn.moneyai.models.dtos.budget.BudgetResponse;
import com.datn.moneyai.models.entities.bases.BudgetEntity;
import com.datn.moneyai.models.entities.bases.CategoryEntity;
import com.datn.moneyai.models.entities.bases.UserEntity;
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

    /**
     * Tạo mới một ngân sách.
     *
     * @param request Dữ liệu đầu vào chứa thông tin ngân sách cần tạo (tháng,
     *                năm, số tiền, danh mục).
     * @return ApiResult mang theo đối tượng BudgetResponse vừa được tạo.
     * @throws UserMessageException Nếu thiếu thông tin bắt buộc hoặc không tìm
     *                              thấy danh mục.
     */
    @Override
    public BudgetResponse createBudget(BudgetRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
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

        BudgetEntity budgetEntity = BudgetEntity.builder()
                .user(user)
                .category(category)
                .limitAmount(request.getLimitAmount())
                .month(request.getMonth())
                .year(request.getYear())
                .build();
        BudgetEntity saved = budgetRepository.save(budgetEntity);
        return toResponse(saved);
    }

    /**
     * Cập nhật thông tin ngân sách.
     *
     * @param id      ID của ngân sách cần cập nhật.
     * @param request Dữ liệu đầu vào chứa thông tin ngân sách cần cập nhật.
     * @return ApiResult mang theo đối tượng BudgetResponse vừa được cập nhật.
     * @throws UserMessageException Nếu không tìm thấy ngân sách hoặc danh mục mới
     *                              không hợp lệ.
     */
    @Override
    public BudgetResponse updateBudget(Long id, BudgetRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserMessageException("Không tìm thấy người dùng."));

        BudgetEntity budgetEntity = budgetRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new UserMessageException("Không tìm thấy ngân sách."));

        if (request.getLimitAmount() != null) {
            budgetEntity.setLimitAmount(request.getLimitAmount());
        }
        if (request.getMonth() != null) {
            budgetEntity.setMonth(request.getMonth());
        }
        if (request.getYear() != null) {
            budgetEntity.setYear(request.getYear());
        }
        if (request.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new UserMessageException("Không tìm thấy danh mục."));
            if (!category.getUser().getId().equals(user.getId())) {
                throw new UserMessageException("Bạn không có quyền gán danh mục này.");
            }
            budgetEntity.setCategory(category);
        }

        budgetRepository.findByUserAndCategoryAndMonthAndYear(user.getId(), budgetEntity.getCategory().getId(),
                        budgetEntity.getMonth(), budgetEntity.getYear())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(budgetEntity.getId())) {
                        throw new UserMessageException("Đã tồn tại ngân sách trùng danh mục/tháng/năm.");
                    }
                });

        BudgetEntity saved = budgetRepository.save(budgetEntity);
        return toResponse(saved);
    }

    /**
     * Lấy thông tin một ngân sách theo ID.
     *
     * @param id ID của ngân sách cần lấy thông tin.
     * @return ApiResult mang theo đối tượng BudgetResponse vừa được lấy.
     * @throws UserMessageException Nếu không tìm thấy ngân sách hoặc người dùng
     *                              không có quyền truy cập.
     */
    @Override
    public BudgetResponse getBudget(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserMessageException("Không tìm thấy người dùng."));

        BudgetEntity budgetEntity = budgetRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new UserMessageException("Không tìm thấy ngân sách."));
        return toResponse(budgetEntity);
    }

    /**
     * Lấy thông tin ngân sách theo danh mục.
     *
     * @param categoryId ID của danh mục.
     * @param month      Tháng cần lọc.
     * @param year       Năm cần lọc.
     * @return ApiResult mang theo đối tượng BudgetResponse vừa được lấy.
     * @throws UserMessageException Nếu không tìm thấy ngân sách hoặc người dùng
     *                              không có quyền truy cập.
     */
    @Override
    public BudgetResponse getBudgetByCategory(Long categoryId, Integer month, Integer year) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserMessageException("Không tìm thấy người dùng."));

        if (categoryId == null) {
            throw new UserMessageException("Thiếu danh mục.");
        }
        LocalDate now = LocalDate.now();
        int m = month != null ? month : now.getMonthValue();
        int y = year != null ? year : now.getYear();

        BudgetEntity budgetEntity = budgetRepository.findByUserAndCategoryAndMonthAndYear(user.getId(), categoryId, m, y)
                .orElseThrow(() -> new UserMessageException("Không tìm thấy ngân sách theo danh mục."));
        return toResponse(budgetEntity);
    }

    /**
     * Lấy danh sách tất cả ngân sách.
     *
     * @param month Tháng cần lọc.
     * @param year  Năm cần lọc.
     * @return ApiResult mang theo danh sách đối tượng BudgetResponse.
     * @throws UserMessageException Nếu không tìm thấy ngân sách hoặc người dùng
     *                              không có quyền truy cập.
     */
    @Override
    public List<BudgetResponse> listBudgets(Integer month, Integer year) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserMessageException("Không tìm thấy người dùng."));

        LocalDate now = LocalDate.now();
        int m = month != null ? month : now.getMonthValue();
        int y = year != null ? year : now.getYear();

        return budgetRepository.findAllByUserAndMonthAndYear(user.getId(), m, y)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     * Xóa ngân sách theo ID.
     *
     * @param id ID của ngân sách cần xóa.
     * @return ApiResult mang theo null nếu xóa thành công.
     * @throws UserMessageException Nếu không tìm thấy ngân sách hoặc người dùng
     *                              không có quyền truy cập.
     */
    @Override
    public void deleteBudget(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserMessageException("Không tìm thấy người dùng."));

        BudgetEntity budgetEntity = budgetRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new UserMessageException("Không tìm thấy ngân sách."));
        budgetRepository.delete(budgetEntity);
    }

    /**
     * Chuyển đổi từ đối tượng Budget sang BudgetResponse.
     *
     * @param budgetEntity Đối tượng Budget cần chuyển đổi.
     * @return Đối tượng BudgetResponse tương ứng với Budget đầu vào.
     */
    private BudgetResponse toResponse(BudgetEntity budgetEntity) {
        BudgetResponse res = new BudgetResponse();
        res.setId(budgetEntity.getId());
        res.setLimitAmount(budgetEntity.getLimitAmount());
        res.setMonth(budgetEntity.getMonth());
        res.setYear(budgetEntity.getYear());
        res.setCategoryId(budgetEntity.getCategory() != null ? budgetEntity.getCategory().getId() : null);
        res.setCategoryName(budgetEntity.getCategory() != null ? budgetEntity.getCategory().getName() : null);
        res.setCreatedAt(budgetEntity.getCreatedAt());
        res.setUpdatedAt(budgetEntity.getUpdatedAt());
        return res;
    }
}
