package com.datn.moneyai.models.dtos.SavingTransaction;

import com.datn.moneyai.models.entities.bases.SavingGoalEntity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class SavingTransactionRequest {

    private Long savingGoalId;

    private BigDecimal amount;

    private LocalDate transactionDate;

    private String note;

}
