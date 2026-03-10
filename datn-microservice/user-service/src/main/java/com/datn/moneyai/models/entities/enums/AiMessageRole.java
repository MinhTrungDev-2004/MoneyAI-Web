package com.datn.moneyai.models.entities.enums;

public enum AiMessageRole {
    USER("Người Dùng"),
    ASSISTANT("Trợ Lý Ảo"),
    SYSTEM("Hệ Thống");

    private final String displayName;

    AiMessageRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}