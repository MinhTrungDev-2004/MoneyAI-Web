package com.datn.moneyai.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.datn.moneyai.models.entities.bases.TransactionEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("SELECT t FROM TransactionEntity t JOIN FETCH t.category WHERE t.id = :id AND t.user.id = :userId")
    Optional<TransactionEntity> findActiveByIdAndUser(@Param("id") Long id,
                                                      @Param("userId") Long userId);

    @Query("SELECT t FROM TransactionEntity t JOIN FETCH t.category WHERE t.category.id = :categoryId AND t.user.id = :userId")
    List<TransactionEntity> findAllActiveByCategoryAndUser(@Param("categoryId") Long categoryId,
                                                           @Param("userId") Long userId);

    @Query("SELECT t FROM TransactionEntity t " +
            "JOIN FETCH t.category c " +
            "WHERE t.user.id = :userId " +
            "AND t.transactionDate >= :startDate " +
            "AND t.transactionDate <= :endDate")
    List<TransactionEntity> findAllByUserAndDateBetween(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT COALESCE(SUM(t.total_amount), 0) FROM transactions t " +
            "INNER JOIN categories c ON t.category_id = c.id " +
            "WHERE t.user_id = :userId " +
            "AND c.type = 'EXPENSE' " +
            "AND EXTRACT(MONTH FROM t.transaction_date) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND EXTRACT(YEAR FROM t.transaction_date) = EXTRACT(YEAR FROM CURRENT_DATE) " +
            "AND t.total_amount > 0", nativeQuery = true)
    BigDecimal calculateTotalExpense(@Param("userId") Long userId);
}
