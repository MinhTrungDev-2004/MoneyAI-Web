package com.datn.moneyai.services.interfaces;

import com.datn.moneyai.models.dtos.users.UserCreateRequest;
import com.datn.moneyai.models.dtos.users.UserGetsResponse;
import java.util.List;

public interface IAuthService {
    Long createUser(UserCreateRequest request);

    List<UserGetsResponse> getUser();

    void logout(String accessToken, String refreshToken);
}
