package com.datn.moneyai.services.interfaces;

import java.util.List;

import com.datn.moneyai.models.dtos.category.CategoryRequest;
import com.datn.moneyai.models.dtos.category.CategoryResponse;

public interface ICategoryService {
    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    List<CategoryResponse> getsCategory();

    void deleteCategory(Long id);
}
