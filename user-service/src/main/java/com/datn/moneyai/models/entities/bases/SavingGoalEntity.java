package com.datn.moneyai.models.entities.bases;

import com.datn.moneyai.models.entities.enums.SavingGoalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "saving_goals")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavingGoalEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "target_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal targetAmount;

    @Builder.Default
    @Column(name = "current_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal currentAmount = BigDecimal.ZERO;

    @Column(name = "deadline_date")
    private LocalDate deadlineDate;

    @Column(name = "icon_name")
    private String iconName;

    @Column(name = "color_class")
    private String colorClass;

    @Column(name = "bg_class")
    private String bgClass;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private SavingGoalStatus status = SavingGoalStatus.ONGOING;

    @Version
    @Column(name = "version")
    private Long version;
}
