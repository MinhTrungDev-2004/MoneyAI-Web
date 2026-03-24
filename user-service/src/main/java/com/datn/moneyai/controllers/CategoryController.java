package com.datn.moneyai.controllers;

import com.datn.moneyai.models.dtos.category.CategoryRequest;
import com.datn.moneyai.models.dtos.category.CategoryResponse;
import com.datn.moneyai.models.dtos.category.CategoryTotalAmount;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.services.interfaces.ICategoryService;
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
@RequestMapping("/api/v1/categories")
@Tag(name = "CategoryController", description = "Quản lý danh mục thu chi của người dùng")
public class CategoryController {
    
    private final ICategoryService categoryService;

    @Operation(summary = "Tạo mới một danh mục")
    @PostMapping
    public ResponseEntity<ApiResult<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request));
    }

    @Operation(summary = "Cập nhật thông tin danh mục")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @Operation(summary = "Lấy danh sách tất cả danh mục của người dùng hiện tại")
    @GetMapping
    public ResponseEntity<ApiResult<List<CategoryResponse>>> getAllCategory() {
        return ResponseEntity.ok(categoryService.getsCategory());
    }

    @Operation(summary = "Tính tổng số tiền giao dịch của tất cả danh mục trong tháng hiện tại")
    @GetMapping("/total-amount")
    public ResponseEntity<ApiResult<List<CategoryTotalAmount>>> getTotalAmountByAllCategoriesThisMonth() {
        return ResponseEntity.ok(categoryService.getTotalAmountByAllCategoriesThisMonth());
    }

    @Operation(summary = "Xóa một danh mục")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
}