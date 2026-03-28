package com.datn.moneyai.models.dtos.saving_transaction;

import com.datn.moneyai.models.entities.enums.SavingTransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavingTransactionResponse {

    private Long savingGoalId;

    private BigDecimal amount;

    private BigDecimal currentAmount;

    private LocalDate transactionDate;

    private SavingTransactionType type;

    private String note;
}
