package com.datn.moneyai.models.dtos.SavingTransaction;

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

    private LocalDate transactionDate;

    private String note;
}
