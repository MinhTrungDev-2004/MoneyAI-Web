package com.datn.moneyai.controllers;

import com.datn.moneyai.models.dtos.saving.SavingGoalRequest;
import com.datn.moneyai.models.dtos.saving.SavingGoalResponse;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.services.implement.SavingGoalService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/saving-goals")
public class SavingGoalController {

    @Autowired
    private SavingGoalService savingGoalService;


    @Operation(summary = "Tạo mới mục tiêu")
    @PostMapping()
    public ResponseEntity<ApiResult<SavingGoalResponse>> createSavingGoal(
            @Valid @RequestBody SavingGoalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(savingGoalService.createSavingGoal(request));
    }

    @Operation(summary = "Lấy danh sách mục tiêu tích kiệm")
    @GetMapping
    public ResponseEntity<ApiResult<List<SavingGoalResponse>>> updateSavingGoal() {
        return ResponseEntity.ok(savingGoalService.getsSavingGoal());
    }
}
