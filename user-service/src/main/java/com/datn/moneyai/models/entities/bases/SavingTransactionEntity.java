package com.datn.moneyai.models.entities.bases;

import com.datn.moneyai.models.entities.enums.SavingTransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "saving_transactions")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingTransactionEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saving_goal_id", nullable = false)
    private SavingGoalEntity savingGoalEntity;

    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20, nullable = false)
    private SavingTransactionType type;

    private String note;
}
