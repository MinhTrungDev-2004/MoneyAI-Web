package com.datn.moneyai.models.dtos.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginGetResponse {
    // Thông tin user trả về hiển thị lên giao diện
    private String fullName;
    private String email;
    private String role;
}
