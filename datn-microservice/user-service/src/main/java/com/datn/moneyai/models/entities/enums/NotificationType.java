package com.datn.moneyai.models.entities.enums;

public enum NotificationType {
    SYSTEM("Hệ Thống"),
    REMINDER("Nhắc Nhở"),
    ALERT("Cảnh Báo"),
    AI_INSIGHT("Phân Tích AI");

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}