package com.datn.moneyai.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.datn.moneyai.models.dtos.budget.BudgetRequest;
import com.datn.moneyai.models.dtos.budget.BudgetResponse;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.services.interfaces.IBudgetService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/budgets")
public class BudgetController {
    private final IBudgetService budgetService;

    /**
     * API tạo mới một ngân sách.
     *
     * @param request Dữ liệu đầu vào chứa thông tin ngân sách cần tạo (tháng,
     *                năm,số tiền, danh mục).
     * @return ResponseEntity chứa ApiResult mang theo đối tượng BudgetResponse vừa
     *         được tạo.
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResult<BudgetResponse>> createBudget(@Valid @RequestBody BudgetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetService.createBudget(request));
    }

    /**
     * API cập nhật thông tin ngân sách.
     *
     * @param id      ID của ngân sách cần cập nhật.
     * @param request Dữ liệu đầu vào chứa thông tin ngân sách cần cập nhật.
     * @return ResponseEntity chứa ApiResult mang theo đối tượng BudgetResponse vừa
     *         được cập nhật.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResult<BudgetResponse>> updateBudget(@PathVariable Long id,
            @Valid @RequestBody BudgetRequest request) {
        return ResponseEntity.ok(budgetService.updateBudget(id, request));
    }

    /**
     * API lấy thông tin một ngân sách theo ID.
     *
     * @param id ID của ngân sách cần lấy thông tin.
     * @return ResponseEntity chứa ApiResult mang theo đối tượng BudgetResponse vừa
     *         được lấy.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResult<BudgetResponse>> getBudget(@PathVariable Long id) {
        return ResponseEntity.ok(budgetService.getBudget(id));
    }

    /**
     * API lấy thông tin ngân sách theo danh mục.
     *
     * @param categoryId ID của danh mục.
     * @param month      Tháng cần lọc.
     * @param year       Năm cần lọc.
     * @return ResponseEntity chứa ApiResult mang theo đối tượng BudgetResponse vừa
     *         được lấy.
     */
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<ApiResult<BudgetResponse>> getByCategory(@PathVariable Long categoryId,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(budgetService.getBudgetByCategory(categoryId, month, year));
    }

    /**
     * API lấy danh sách tất cả ngân sách.
     * 
     * @param month Tháng cần lọc.
     * @param year  Năm cần lọc.
     * @return ResponseEntity chứa ApiResult mang theo danh sách đối tượng
     *         BudgetResponse.
     */
    @GetMapping("/gets-all")
    public ResponseEntity<ApiResult<List<BudgetResponse>>> listBudgets(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(budgetService.listBudgets(month, year));
    }

    /**
     * API xóa một ngân sách theo ID.
     *
     * @param id ID của ngân sách cần xóa.
     * @return ResponseEntity chứa ApiResult mang theo thông báo kết quả xóa.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResult<Void>> deleteBudget(@PathVariable Long id) {
        return ResponseEntity.ok(budgetService.deleteBudget(id));
    }
}
