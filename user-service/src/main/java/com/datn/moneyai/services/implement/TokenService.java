package com.datn.moneyai.services.implement;

import com.datn.moneyai.exceptions.ResourceNotFoundException;
import com.datn.moneyai.models.dtos.auth.LoginGetResponse;
import com.datn.moneyai.models.dtos.auth.TokenResponse;
import com.datn.moneyai.models.entities.bases.UserEntity;
import com.datn.moneyai.repositories.UserRepository;
import com.datn.moneyai.security.JwtTokenProvider;
import com.datn.moneyai.services.interfaces.ITokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;

    @Override
    public TokenResponse generateTokens(UserDetails userDetails) {
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);
        long ttlSeconds = Math.max(0,
                (jwtTokenProvider.extractExpiration(refreshToken).getTime() - System.currentTimeMillis()) / 1000);
        if (ttlSeconds > 0) {
            String redisKey = "refreshToken:" + userDetails.getUsername();
            redisTemplate.opsForValue().set(redisKey, refreshToken, ttlSeconds, TimeUnit.SECONDS);
        }
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.extractExpiration(accessToken).getTime())
                .build();
    }


    @Override
    public LoginGetResponse getUserInfo(Long userId) {
        UserEntity user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng."));

        LoginGetResponse response = LoginGetResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getUserRoleEntities().stream().findFirst().map(ur -> ur.getRoleEntity().getName().name()).orElse(null))
                .build();

        return response;
    }
}
