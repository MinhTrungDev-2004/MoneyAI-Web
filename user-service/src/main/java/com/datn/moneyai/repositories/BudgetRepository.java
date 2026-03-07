package com.datn.moneyai.repositories;

import com.datn.moneyai.models.entities.bases.Budget;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query("SELECT b FROM Budget b WHERE b.id = :id AND b.user.id = :userId")
    Optional<Budget> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT b FROM Budget b WHERE b.user.id = :userId AND b.category.id = :categoryId AND b.month = :month AND b.year = :year")
    Optional<Budget> findByUserAndCategoryAndMonthAndYear(@Param("userId") Long userId,
                                                          @Param("categoryId") Long categoryId,
                                                          @Param("month") Integer month,
                                                          @Param("year") Integer year);

    @Query("SELECT b FROM Budget b WHERE b.user.id = :userId AND b.month = :month AND b.year = :year")
    List<Budget> findAllByUserAndMonthAndYear(@Param("userId") Long userId,
                                              @Param("month") Integer month,
                                              @Param("year") Integer year);
}
