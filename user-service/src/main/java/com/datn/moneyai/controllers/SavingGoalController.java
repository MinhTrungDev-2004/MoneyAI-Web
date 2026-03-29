package com.datn.moneyai.controllers;

import com.datn.moneyai.models.dtos.saving.SavingGoalRequest;
import com.datn.moneyai.models.dtos.saving.SavingGoalResponse;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.services.interfaces.ISavingGoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/saving-goals")
@Tag(name = "SavingGoalController", description = "Mục tiêu tích kiệm")
@RequiredArgsConstructor
public class SavingGoalController {

    private final ISavingGoalService savingGoalService;

    @Operation(summary = "Tạo mới mục tiêu")
    @PostMapping()
    public ResponseEntity<ApiResult<SavingGoalResponse>> createSavingGoal(
            @Valid @RequestBody SavingGoalRequest request) {
        SavingGoalResponse response = savingGoalService.createSavingGoal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResult.success(response, "Tạo mới mục tiêu thành công"));
    }

    @Operation(summary = "Cập nhật thông tin mục tiêu tích kiệm")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<SavingGoalResponse>> updateSavingGoal(
            @PathVariable Long id,
            @RequestBody SavingGoalRequest request) {
        SavingGoalResponse response = savingGoalService.updateSavingGoal(id, request);
        return ResponseEntity.ok(ApiResult.success(response, "Cập nhật thành công"));
    }

    @Operation(summary = "Lấy danh sách mục tiêu tích kiệm")
    @GetMapping
    public ResponseEntity<ApiResult<List<SavingGoalResponse>>> getSavingGoals() {
        List<SavingGoalResponse> response = savingGoalService.getsSavingGoal();
        return ResponseEntity.ok(ApiResult.success(response, "Lấy danh sách mục tiêu tích kiệm thành công"));
    }

    @Operation(summary = "Xóa mục tiêu tích kiệm")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResult<Void>> deleteSavingGoal(@PathVariable Long id) {
        savingGoalService.deleteSavingGoal(id);
        return ResponseEntity.ok(ApiResult.success(null, "Xóa mục tiêu tích kiệm thành công"));
    }
}
