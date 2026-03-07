package com.datn.moneyai.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.datn.moneyai.models.dtos.budget.BudgetRequest;
import com.datn.moneyai.models.dtos.budget.BudgetResponse;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.services.interfaces.IBudgetService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@AllArgsConstructor
@RequestMapping("/budget")
public class BudgetController extends ApiBaseController {
    private final IBudgetService budgetService;

    /*
     * API tạo mới budget
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResult<BudgetResponse>> createBudget(@RequestBody BudgetRequest request) {
        return exeResponseEntity(() -> ApiResult.success(
                budgetService.createBudget(request),
                "Tạo ngân sách thành công"));
    }

    /*
     * API cập nhật budget
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResult<BudgetResponse>> updateBudget(@PathVariable Long id,
            @RequestBody BudgetRequest request) {
        return exeResponseEntity(() -> ApiResult.success(
                budgetService.updateBudget(id, request),
                "Cập nhật ngân sách thành công"));
    }

    /*
     * API lấy ngân sách theo id
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResult<BudgetResponse>> getBudget(@PathVariable Long id) {
        return exeResponseEntity(() -> ApiResult.success(
                budgetService.getBudget(id), "Lấy ngân sách thành công"));
    }

    /*
     * API lấy ngân sách theo danh mục (mặc định tháng/năm hiện tại)
     */
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<ApiResult<BudgetResponse>> getByCategory(@PathVariable Long categoryId,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        return exeResponseEntity(() -> ApiResult.success(
                budgetService.getBudgetByCategory(categoryId, month, year),
                "Lấy ngân sách theo danh mục thành công"));
    }

    /*
     * API lấy danh sách ngân sách theo tháng/năm
     */
    @GetMapping("/gets-all")
    public ResponseEntity<ApiResult<java.util.List<BudgetResponse>>> listBudgets(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        return exeResponseEntity(() -> ApiResult.success(
                budgetService.listBudgets(month, year),
                "Lấy danh sách ngân sách thành công"));
    }

    /*
     * API xóa ngân sách
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResult<Void>> deleteBudget(@PathVariable Long id) {
        return exeResponseEntity(() -> {
            budgetService.deleteBudget(id);
            return ApiResult.success(null, "Xóa ngân sách thành công");
        });
    }
}
