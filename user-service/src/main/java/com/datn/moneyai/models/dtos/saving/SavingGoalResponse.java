package com.datn.moneyai.models.dtos.saving;

import com.datn.moneyai.models.entities.enums.SavingGoalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingGoalResponse {
    private Long id;

    private String name;

    private BigDecimal targetAmount;

    private LocalDate deadlineDate;

    private SavingGoalStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
