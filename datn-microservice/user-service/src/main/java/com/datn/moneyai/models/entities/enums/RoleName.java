package com.datn.moneyai.models.entities.enums;

public enum RoleName {
    USER("Người Dùng"),
    ADMIN("Quản Trị Viên");

    private final String displayName;

    RoleName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
