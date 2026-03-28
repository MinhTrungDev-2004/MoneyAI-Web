package com.datn.moneyai.models.entities.enums;

public enum SavingTransactionType {
    DEPOSIT("Nạp Thêm"),
    WITHDRAWAL("Rút bớt");

    private final String displayName;

    SavingTransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
