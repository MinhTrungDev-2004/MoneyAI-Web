package com.datn.moneyai.controllers;

import com.datn.moneyai.models.dtos.auth.LoginRequest;
import com.datn.moneyai.models.dtos.auth.TokenResponse;
import com.datn.moneyai.models.dtos.users.UserCreateRequest;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.models.dtos.auth.LoginGetResponse;
import com.datn.moneyai.models.security.UserPrincipal;
import com.datn.moneyai.services.interfaces.ITokenService;
import com.datn.moneyai.services.interfaces.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Xác Thực - AuthController", description = "Quản lý đăng nhập, đăng ký và phiên hoạt động của người dùng")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final ITokenService tokenService;
    private final IAuthService authService;

    @Operation(summary = "Đăng ký tài khoản mới")
    @PostMapping("/register")
    public ResponseEntity<ApiResult<Long>> register(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.createUser(request));
    }

    @Operation(summary = "Đăng nhập", description = "Trả về AccessToken trong Body và RefreshToken ẩn an toàn trong HttpOnly Cookie")
    @PostMapping("/login")
    public ResponseEntity<ApiResult<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            ApiResult<TokenResponse> tokens = tokenService.generateTokens(userDetails);

            // Lưu RefreshToken vào Cookie
            ResponseCookie cookie = ResponseCookie.from("refreshToken", tokens.getData().getRefreshToken())
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60) // Sống 7 ngày
                    .sameSite("Strict")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(ApiResult.success(tokens.getData(), "Đăng nhập thành công"));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResult.fail("Email hoặc mật khẩu không chính xác"));
        }
    }

    @Operation(summary = "Lấy thông tin tài khoản đang đăng nhập", description = "Cần gửi kèm Access Token trên Header")
    @GetMapping("/me")
    public ResponseEntity<ApiResult<LoginGetResponse>> getCurrentUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ApiResult<LoginGetResponse> result = tokenService.getUserInfo(userPrincipal.getId());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Đăng xuất", description = "Xóa RefreshToken trong Cookie và vô hiệu hóa AccessToken")
    @PostMapping("/logout")
    public ResponseEntity<ApiResult<Void>> logout(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @CookieValue(name = "refreshToken", required = false) String refreshToken) {

        String accessToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.substring(7);
        }
        authService.logout(accessToken, refreshToken);

        // Xóa cookie bằng cách set maxAge = 0
        ResponseCookie clearCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false) // Tương tự như lúc set cookie
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearCookie.toString())
                .body(ApiResult.success(null, "Đăng xuất thành công"));
    }
}