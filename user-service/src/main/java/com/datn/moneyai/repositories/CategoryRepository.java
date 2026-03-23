package com.datn.moneyai.repositories;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import com.datn.moneyai.models.entities.bases.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    @Query(value = "SELECT * FROM categories WHERE id = :id", nativeQuery = true)
    Optional<CategoryEntity> findCategoryById(@Param("id") Long id);

    @Query(value = "SELECT * FROM categories WHERE id = :id", nativeQuery = true)
    Optional<CategoryEntity> findActiveCategoryById(@Param("id") Long id);

    @Query(value = "SELECT * FROM categories", nativeQuery = true)
    List<CategoryEntity> findAllActiveCategories();

    @Query(value = "SELECT c.id AS categoryId, c.name AS categoryName, c.type AS categoryType, c.icon AS icon, c.color_code AS colorCode, COALESCE(SUM(t.total_amount), 0) AS totalAmount " +
            "FROM categories c " +
            "LEFT JOIN transactions t ON c.id = t.category_id " +
            "AND t.user_id = :userId " +
            "AND EXTRACT(MONTH FROM t.transaction_date) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND EXTRACT(YEAR FROM t.transaction_date) = EXTRACT(YEAR FROM CURRENT_DATE) " +
            "WHERE c.user_id = :userId " +
            "GROUP BY c.id, c.name, c.type, c.icon, c.color_code", nativeQuery = true)
    List<Object[]> sumTotalAmountByAllCategoriesInMonth(@Param("userId") Long userId);
}
