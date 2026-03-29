package com.datn.moneyai.controllers;

import com.datn.moneyai.models.dtos.transaction.TransactionRequest;
import com.datn.moneyai.models.dtos.transaction.TransactionResponse;
import com.datn.moneyai.models.dtos.transaction.TransactionUpdateRequest;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.services.interfaces.ITransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.time.Year;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "TransactionController", description = "Quản lý thu chi và lịch sử giao dịch")
@RequiredArgsConstructor
public class TransactionController {

    private final ITransactionService transactionService;

    @Operation(summary = "Tạo mới một giao dịch")
    @PostMapping
    public ResponseEntity<ApiResult<TransactionResponse>> createTransaction(
            @Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.success(response, "Tạo giao dịch thành công"));
    }

    @Operation(summary = "Cập nhật thông tin giao dịch")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<TransactionResponse>> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionUpdateRequest request) {
        TransactionResponse response = transactionService.updateTransaction(id, request);
        return ResponseEntity.ok(ApiResult.success(response, "Cập nhật giao dịch thành công"));
    }

    @Operation(summary = "Xóa một giao dịch")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok(ApiResult.success(null, "Xóa giao dịch thành công"));
    }

    @Operation(summary = "Lấy danh sách giao dịch theo tháng")
    @GetMapping("/by-month")
    public ResponseEntity<ApiResult<List<TransactionResponse>>> getTransactionsByMonth(
            @RequestParam("monthYear") @DateTimeFormat(pattern = "M/yyyy") YearMonth monthYear) {
        List<TransactionResponse> response = transactionService.getTransactionsByMonth(monthYear);
        return ResponseEntity.ok(ApiResult.success(response, "Lấy danh sách giao dịch theo tháng thành công"));
    }

    @Operation(summary = "Lấy danh sách giao dịch theo năm")
    @GetMapping("/by-year")
    public ResponseEntity<ApiResult<List<TransactionResponse>>> getTransactionsByYear(
            @RequestParam("Year") @DateTimeFormat(pattern = "yyyy") Year Year) {
        List<TransactionResponse> response = transactionService.getTransactionsByYear(Year);
        return ResponseEntity.ok(ApiResult.success(response, "Lấy danh sách giao dịch năm " + Year.getValue() + " thành công"));
    }

    @Operation(summary = "Lấy danh sách giao dịch theo danh mục")
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResult<List<TransactionResponse>>> getTransactionsByCategory(
            @PathVariable Long categoryId) {
        List<TransactionResponse> response = transactionService.getTransactionsByCategory(categoryId);
        return ResponseEntity.ok(ApiResult.success(response, "Lấy danh sách giao dịch theo danh mục thành công"));
    }
}
