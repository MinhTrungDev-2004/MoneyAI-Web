package com.datn.moneyai.models.dtos.saving_transaction;

import com.datn.moneyai.models.entities.enums.SavingTransactionType;
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

    private SavingTransactionType type;

    private String note;

}
