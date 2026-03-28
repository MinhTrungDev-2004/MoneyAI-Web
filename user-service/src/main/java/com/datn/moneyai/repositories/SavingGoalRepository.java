package com.datn.moneyai.repositories;


import com.datn.moneyai.models.entities.bases.SavingGoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SavingGoalRepository extends JpaRepository<SavingGoalEntity, Long> {
    @Query(value = "SELECT * FROM saving_goals WHERE user_id = :id", nativeQuery = true)
    List<SavingGoalEntity> findByUser(@Param("id") Long id);

    @Query(value = "SELECT * FROM saving_goals WHERE  user_id = :userId AND id = :id", nativeQuery = true)
    Optional<SavingGoalEntity> findByIdAndUserId(@Param("userId") Long userId, @Param("id") Long id);
}
