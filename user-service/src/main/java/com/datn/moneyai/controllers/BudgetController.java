package com.datn.moneyai.controllers;

import com.datn.moneyai.models.dtos.budget.BudgetRequest;
import com.datn.moneyai.models.dtos.budget.BudgetResponse;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.services.interfaces.IBudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/budgets")
@Tag(name = "BudgetController", description = "Quản lý ngân sách và mục tiêu tiết kiệm")
public class BudgetController {

    private final IBudgetService budgetService;

    @Operation(summary = "Tạo mới một ngân sách")
    @PostMapping
    public ResponseEntity<ApiResult<BudgetResponse>> createBudget(@Valid @RequestBody BudgetRequest request) {
        BudgetResponse response = budgetService.createBudget(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResult.success(response, "Tạo ngân sách thành công"));
    }

    @Operation(summary = "Cập nhật thông tin ngân sách")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<BudgetResponse>> updateBudget(
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequest request) {
        BudgetResponse response = budgetService.updateBudget(id, request);
        return ResponseEntity.ok(ApiResult.success(response, "Cập nhật ngân sách thành công"));
    }

    @Operation(summary = "Lấy chi tiết một ngân sách theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<BudgetResponse>> getBudget(@PathVariable Long id) {
        BudgetResponse response = budgetService.getBudget(id);
        return ResponseEntity.ok(ApiResult.success(response, "Lấy ngân sách thành công"));
    }

    @Operation(summary = "Lấy thông tin ngân sách theo danh mục")
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResult<BudgetResponse>> getByCategory(
            @PathVariable Long categoryId,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        BudgetResponse response = budgetService.getBudgetByCategory(categoryId, month, year);
        return ResponseEntity.ok(ApiResult.success(response, "Lấy ngân sách theo danh mục thành công"));
    }

    @Operation(summary = "Lấy danh sách tất cả ngân sách")
    @GetMapping
    public ResponseEntity<ApiResult<List<BudgetResponse>>> listBudgets(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        List<BudgetResponse> response = budgetService.listBudgets(month, year);
        return ResponseEntity.ok(ApiResult.success(response, "Lấy danh sách ngân sách thành công"));
    }

    @Operation(summary = "Xóa một ngân sách")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.ok(ApiResult.success(null, "Xóa ngân sách thành công"));
    }
}
