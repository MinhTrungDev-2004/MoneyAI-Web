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

    @Query(value = "SELECT * FROM transactions WHERE id = :id AND user_id = :userId", nativeQuery = true)
    Optional<TransactionEntity> findActiveByIdAndUser(@Param("id") Long id,
                                                      @Param("userId") Long userId);

    @Query(value = "SELECT * FROM transactions WHERE category_id = :categoryId AND user_id = :userId", nativeQuery = true)
    List<TransactionEntity> findAllActiveByCategoryAndUser(@Param("categoryId") Long categoryId,
                                                           @Param("userId") Long userId);

    @Query(value = "SELECT COALESCE(SUM(total_amount), 0) FROM transactions " +
            "WHERE category_id = :categoryId AND user_id = :userId " +
            "AND transaction_date >= DATE_TRUNC('month', CURRENT_DATE) " +
            "AND transaction_date < DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month'",
            nativeQuery = true)
    BigDecimal sumTotalAmountByCategoryAndMonth(@Param("categoryId") Long categoryId,
                                                @Param("userId") Long userId);

    @Query(value = "SELECT * FROM transactions WHERE user_id = :userId AND DATE(transaction_date) = :transactionDate", nativeQuery = true)
    List<TransactionEntity> findAllByUserAndDate(
            @Param("userId") Long userId,
            @Param("transactionDate") LocalDate transactionDate);

    @Query(value = "SELECT COALESCE(SUM(t.total_amount), 0) FROM transactions t " +
            "INNER JOIN categories c ON t.category_id = c.id " +
            "WHERE t.user_id = :userId " +
            "AND c.type = 'INCOME' " +
            "AND EXTRACT(MONTH FROM t.transaction_date) = EXTRACT(MONTH FROM CURRENT_DATE)" +
            "AND EXTRACT(YEAR FROM t.transaction_date) = EXTRACT(YEAR FROM CURRENT_DATE) " +
            "AND t.total_amount > 0", nativeQuery = true)
    BigDecimal calculateTotalIncome(@Param("userId") Long userId);

    @Query(value = "SELECT COALESCE(SUM(t.total_amount), 0) FROM transactions t " +
            "INNER JOIN categories c ON t.category_id = c.id " +
            "WHERE t.user_id = :userId " +
            "AND c.type = 'EXPENSE' " +
            "AND EXTRACT(MONTH FROM t.transaction_date) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND EXTRACT(YEAR FROM t.transaction_date) = EXTRACT(YEAR FROM CURRENT_DATE) " +
            "AND t.total_amount > 0", nativeQuery = true)
    BigDecimal calculateTotalExpense(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM transactions " +
            "WHERE user_id = :userId " +
            "AND DATE(transaction_date) = :targetDate", nativeQuery = true)
    List<TransactionEntity> findTransactionsByDate(@Param("userId") Long userId,
                                                   @Param("targetDate") LocalDate targetDate);
}
