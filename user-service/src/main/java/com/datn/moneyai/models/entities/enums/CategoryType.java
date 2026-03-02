package com.datn.moneyai.models.entities.enums;


public enum CategoryType {
    INCOME("Thu Nhập"),
    EXPENSE("Chi Tiêu");

    private final String displayName;

    CategoryType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}