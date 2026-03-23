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
@Tag(name = "Mục Tiêu Tiết Kiệm - BudgetController", description = "Quản lý ngân sách và mục tiêu tiết kiệm")
public class BudgetController {

    private final IBudgetService budgetService;

    @Operation(summary = "Tạo mới một ngân sách")
    @PostMapping
    public ResponseEntity<ApiResult<BudgetResponse>> createBudget(@Valid @RequestBody BudgetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetService.createBudget(request));
    }

    @Operation(summary = "Cập nhật thông tin ngân sách")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<BudgetResponse>> updateBudget(
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequest request) {
        return ResponseEntity.ok(budgetService.updateBudget(id, request));
    }

    @Operation(summary = "Lấy chi tiết một ngân sách theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<BudgetResponse>> getBudget(@PathVariable Long id) {
        return ResponseEntity.ok(budgetService.getBudget(id));
    }

    @Operation(summary = "Lấy thông tin ngân sách theo danh mục")
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResult<BudgetResponse>> getByCategory(
            @PathVariable Long categoryId,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(budgetService.getBudgetByCategory(categoryId, month, year));
    }

    @Operation(summary = "Lấy danh sách tất cả ngân sách")
    @GetMapping
    public ResponseEntity<ApiResult<List<BudgetResponse>>> listBudgets(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(budgetService.listBudgets(month, year));
    }

    @Operation(summary = "Xóa một ngân sách")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> deleteBudget(@PathVariable Long id) {
        return ResponseEntity.ok(budgetService.deleteBudget(id));
    }
}