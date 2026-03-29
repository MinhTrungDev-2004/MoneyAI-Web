package com.datn.moneyai.controllers;

import com.datn.moneyai.models.dtos.saving_transaction.SavingGoalDetailResponse;
import com.datn.moneyai.models.dtos.saving_transaction.SavingTransactionRequest;
import com.datn.moneyai.models.dtos.saving_transaction.SavingTransactionResponse;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.services.interfaces.ISavingTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/saving-transactions")
@Tag(name = "SavingTransactionController", description = "Quản lý giao dịch mục tiêu tích kiệm")
@RequiredArgsConstructor
public class SavingTransactionController {

    private final ISavingTransactionService savingTransactionService;

    @Operation(summary = "Tạo mới một giao dịch tích kiệm")
    @PostMapping
    public ResponseEntity<ApiResult<SavingTransactionResponse>> createSavingTransaction(
            @Valid @RequestBody SavingTransactionRequest request) {
        SavingTransactionResponse response = savingTransactionService.createSavingTransaction(request);
        return ResponseEntity.ok(ApiResult.success(response, "Tạo mới giao dịch quỹ thành công"));
    }

    @Operation(summary = "Lấy giao dịch mục tiêu tích kiệm")
    @GetMapping("/{savingGoalId}")
    public ResponseEntity<ApiResult<SavingGoalDetailResponse>> getSavingTransaction(@PathVariable Long savingGoalId) {
        SavingGoalDetailResponse response = savingTransactionService.getSavingTransaction(savingGoalId);
        return ResponseEntity.ok(ApiResult.success(response, "Lấy chi tiết giao dịch mục tiêu tiết kiệm thành công"));
    }
}
