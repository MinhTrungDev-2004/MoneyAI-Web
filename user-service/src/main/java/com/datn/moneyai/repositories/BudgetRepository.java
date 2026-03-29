package com.datn.moneyai.repositories;

import com.datn.moneyai.models.entities.bases.BudgetEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {

    @Query("SELECT b FROM BudgetEntity b JOIN FETCH b.category WHERE b.id = :id AND b.user.id = :userId")
    Optional<BudgetEntity> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT b FROM BudgetEntity b JOIN FETCH b.category WHERE b.user.id = :userId AND b.category.id = :categoryId AND b.month = :month AND b.year = :year")
    Optional<BudgetEntity> findByUserAndCategoryAndMonthAndYear(@Param("userId") Long userId,
                                                                @Param("categoryId") Long categoryId,
                                                                @Param("month") Integer month,
                                                                @Param("year") Integer year);

    @Query("SELECT DISTINCT b FROM BudgetEntity b JOIN FETCH b.category WHERE b.user.id = :userId AND b.month = :month AND b.year = :year")
    List<BudgetEntity> findAllByUserAndMonthAndYear(@Param("userId") Long userId,
                                                    @Param("month") Integer month,
                                                    @Param("year") Integer year);
}
