package datn.category_service.repositories;

import datn.category_service.models.entities.bases.CategoryEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> id(Long id);

    Optional<CategoryEntity> findById(Long id);
}
