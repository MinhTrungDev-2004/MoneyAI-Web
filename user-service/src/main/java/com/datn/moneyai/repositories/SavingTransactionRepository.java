package com.datn.moneyai.repositories;

import com.datn.moneyai.models.entities.bases.SavingTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingTransactionRepository extends JpaRepository<SavingTransactionEntity, Long> {

}
