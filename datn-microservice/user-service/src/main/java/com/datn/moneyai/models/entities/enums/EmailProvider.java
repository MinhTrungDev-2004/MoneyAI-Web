package com.datn.moneyai.models.entities.enums;

public enum EmailProvider {
    GMAIL("Gmail"),
    OUTLOOK("Outlook"),
    YAHOO("Yahoo"),
    OTHER("Khác");

    private final String displayName;

    EmailProvider(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
