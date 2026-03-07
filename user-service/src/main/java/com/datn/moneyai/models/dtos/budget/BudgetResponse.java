package com.datn.moneyai.models.dtos.budget;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BudgetResponse {

    private Long id;

    private BigDecimal limitAmount;

    private Integer month;

    private Integer year;

    private Long categoryId;

    private String categoryName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
