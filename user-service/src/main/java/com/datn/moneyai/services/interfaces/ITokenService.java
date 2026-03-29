package com.datn.moneyai.services.interfaces;

import com.datn.moneyai.models.dtos.auth.LoginGetResponse;
import com.datn.moneyai.models.dtos.auth.TokenResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface ITokenService {
    TokenResponse generateTokens(UserDetails userDetails);

    LoginGetResponse getUserInfo(Long userId);
}
