package datn.category_service.services.Interfaces;

import datn.category_service.models.dtos.category.CategoryCreateRequest;
import datn.category_service.models.entities.bases.CategoryEntity;

import java.util.Optional;

public interface ICategoryService {
    String createCategory(CategoryCreateRequest request);

    Optional<CategoryEntity> updateCategory(CategoryCreateRequest request);
}
