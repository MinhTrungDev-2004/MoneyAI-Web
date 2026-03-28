package com.datn.moneyai.controllers;

import com.datn.moneyai.models.dtos.SavingTransaction.SavingTransactionRequest;
import com.datn.moneyai.models.dtos.SavingTransaction.SavingTransactionResponse;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.services.interfaces.ISavingTransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/saving-transactions")
public class SavingTransactionController {

    @Autowired
    private ISavingTransactionService savingTransactionService;

    @PostMapping
    public ResponseEntity<ApiResult<SavingTransactionResponse>> createSavingTransaction(
            @Valid @RequestBody SavingTransactionRequest request) {
        return ResponseEntity.ok(savingTransactionService.createSavingTransaction(request));
    }
}
