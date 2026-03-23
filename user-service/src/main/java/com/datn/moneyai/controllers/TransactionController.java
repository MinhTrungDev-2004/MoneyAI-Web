package com.datn.moneyai.controllers;

import com.datn.moneyai.models.dtos.transaction.TransactionRequest;
import com.datn.moneyai.models.dtos.transaction.TransactionResponse;
import com.datn.moneyai.models.dtos.transaction.TransactionUpdateRequest;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.services.interfaces.ITransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
@Tag(name = "Giao Dịch - TransactionController", description = "Quản lý thu chi và lịch sử giao dịch")
public class TransactionController {

    private final ITransactionService transactionService;

    @Operation(summary = "Tạo mới một giao dịch")
    @PostMapping
    public ResponseEntity<ApiResult<TransactionResponse>> createTransaction(
            @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.createTransaction(request));
    }

    @Operation(summary = "Cập nhật thông tin giao dịch")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<TransactionResponse>> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionUpdateRequest request) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, request));
    }

    @Operation(summary = "Xóa một giao dịch")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> deleteTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.deleteTransaction(id));
    }

    @Operation(summary = "Lấy danh sách giao dịch (Lọc theo ngày)")
    @GetMapping
    public ResponseEntity<ApiResult<List<TransactionResponse>>> getTransactionsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(transactionService.getTransactionsByDate(date));
    }

    @Operation(summary = "Lấy danh sách giao dịch theo danh mục")
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResult<List<TransactionResponse>>> getTransactionsByCategory(
            @PathVariable Long categoryId) {
        return ResponseEntity.ok(transactionService.getTransactionsByCategory(categoryId));
    }

    @Operation(summary = "Tính tổng số tiền giao dịch của một danh mục trong tháng hiện tại")
    @GetMapping("/categories/{categoryId}/total-amount")
    public ResponseEntity<ApiResult<BigDecimal>> getTotalAmountByCategoryAndMonth(
            @PathVariable Long categoryId) {
        return ResponseEntity.ok(transactionService.getTotalAmountByCategoryAndMonth(categoryId));
    }

    @Operation(summary = "Tính tổng thu nhập trong tháng hiện tại")
    @GetMapping("/summary/income")
    public ResponseEntity<ApiResult<BigDecimal>> calculateTotalIncome() {
        return ResponseEntity.ok(transactionService.calculateTotalIncome());
    }

    @Operation(summary = "Tính tổng chi tiêu trong tháng hiện tại")
    @GetMapping("/summary/expense")
    public ResponseEntity<ApiResult<BigDecimal>> calculateTotalExpense() {
        return ResponseEntity.ok(transactionService.calculateTotalExpense());
    }
}