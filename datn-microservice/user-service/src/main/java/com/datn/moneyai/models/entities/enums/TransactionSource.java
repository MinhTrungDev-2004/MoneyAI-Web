package com.datn.moneyai.models.entities.enums;

public enum TransactionSource {
    MANUAL("Nhập Thủ Công"),
    EMAIL_PARSER("Quét Từ Email"),
    AI_CHAT("Trợ Lý AI Tạo"),
    BANK_SYNC("Đồng Bộ Ngân Hàng");

    private final String displayName;

    TransactionSource(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}