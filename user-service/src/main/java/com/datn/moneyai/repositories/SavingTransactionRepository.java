package com.datn.moneyai.repositories;

import com.datn.moneyai.models.entities.bases.SavingTransactionEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingTransactionRepository extends JpaRepository<SavingTransactionEntity, Long> {
    @Query(value = "SELECT * FROM saving_transactions WHERE saving_goal_id = :savingGoalId", nativeQuery = true)
    List<SavingTransactionEntity> findBySavingGoalId(@Param("savingGoalId") Long savingGoalId);
}
