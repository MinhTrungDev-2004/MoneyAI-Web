package com.datn.moneyai.models.entities.bases;

import com.datn.moneyai.models.entities.User;
import com.datn.moneyai.models.entities.enums.TransactionSource;
import datn.category_service.models.entities.bases.CategoryEntity;
import jakarta.persistence.*;

public class TransactionEntity extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Column(nullable = false)
    private java.math.BigDecimal amount;

    @Column(nullable = false)
    private java.time.LocalDateTime transactionDate;

    private String note;

    @Enumerated(EnumType.STRING)
    private TransactionSource source;

    private String referenceId;

    private Boolean isDeleted = false;
}
