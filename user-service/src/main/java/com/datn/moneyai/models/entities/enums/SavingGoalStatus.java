package com.datn.moneyai.models.entities.enums;

public enum SavingGoalStatus {
    ONGOING("Đang Thực Hiện"),
    COMPLETED("Đã Hoàn Thành");

    private final String displayName;

    SavingGoalStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}