package com.datn.moneyai.controllers;

import com.datn.moneyai.models.dtos.category.CategoryRequest;
import com.datn.moneyai.models.dtos.category.CategoryResponse;
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
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResult.success(response, "Tạo danh mục thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResult.success(response, "Cập nhật danh mục thành công"));
    }

    @Operation(summary = "Lấy danh sách tất cả danh mục của người dùng hiện tại")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResult<List<CategoryResponse>>> getsCategory(@PathVariable("userId") Long userId) {
        List<CategoryResponse> response = categoryService.getsCategory(userId);
        return ResponseEntity.ok(ApiResult.success(response, "Lấy danh mục thành công"));
    }

    @Operation(summary = "Xóa một danh mục")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResult.success(null, "Xóa danh mục thành công"));
    }
}
