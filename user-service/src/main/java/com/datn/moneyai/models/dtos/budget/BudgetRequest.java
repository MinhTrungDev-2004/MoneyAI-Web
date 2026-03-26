package com.datn.moneyai.models.dtos.budget;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BudgetRequest {

    private BigDecimal limitAmount;

    private Integer month;

    private Integer year;

    private Long categoryId;
}