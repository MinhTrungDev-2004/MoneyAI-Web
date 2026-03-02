package com.datn.moneyai.models.entities.bases;

import com.datn.moneyai.models.entities.User;
import com.datn.moneyai.models.entities.enums.SavingGoalStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "saving_goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingGoalEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private java.math.BigDecimal targetAmount;

    private java.time.LocalDate deadlineDate;

    @Enumerated(EnumType.STRING)
    private SavingGoalStatus status;

    private Boolean isDeleted = false;
}
