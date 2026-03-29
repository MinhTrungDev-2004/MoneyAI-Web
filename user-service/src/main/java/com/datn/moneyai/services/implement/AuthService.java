package com.datn.moneyai.services.implement;

import com.datn.moneyai.exceptions.UserMessageException;
import com.datn.moneyai.models.constant.DefaultCategoryData;
import com.datn.moneyai.models.dtos.users.UserCreateRequest;
import com.datn.moneyai.models.dtos.users.UserGetsResponse;
import com.datn.moneyai.models.entities.bases.CategoryEntity;
import com.datn.moneyai.models.entities.bases.RoleEntity;
import com.datn.moneyai.models.entities.bases.UserEntity;
import com.datn.moneyai.models.entities.bases.UserRoleEntity;
import com.datn.moneyai.models.entities.enums.CategoryType;
import com.datn.moneyai.models.entities.enums.RoleName;
import com.datn.moneyai.models.redis.TokenBlackList;
import com.datn.moneyai.models.redis.TokenBlackListRepository;
import com.datn.moneyai.security.JwtTokenProvider;
import com.datn.moneyai.repositories.CategoryRepository;
import com.datn.moneyai.repositories.RoleRepository;
import com.datn.moneyai.repositories.UserRepository;
import com.datn.moneyai.services.interfaces.IAuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlackListRepository tokenBlackListRepository;
    private final StringRedisTemplate redisTemplate;
    private final CategoryRepository categoryRepository;

    /**
     * Đăng ký tài khoản người dùng thường (luôn gán ROLE_USER; không cho client tự chọn role).
     */
    @Override
    @Transactional
    public Long createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserMessageException("Email đã tồn tại.");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity newUser = UserEntity.builder()
                .email(request.getEmail())
                .password(hashedPassword)
                .isActive(true)
                .build();

        RoleEntity userRoleEntityEntity = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new UserMessageException("Lỗi hệ thống: Không tìm thấy quyền USER."));

        UserRoleEntity userRoleEntity = UserRoleEntity.builder()
                .user(newUser)
                .roleEntity(userRoleEntityEntity)
                .build();
        newUser.setUserRoleEntities(Set.of(userRoleEntity));

        userRepository.save(newUser);

        List<CategoryEntity> defaultCategories = DefaultCategoryData.DEFAULT_CATEGORIES.stream()
                .map(data -> CategoryEntity.builder()
                        .user(newUser)
                        .type(CategoryType.valueOf(data.type()))
                        .name(data.name())
                        .icon(data.icon())
                        .colorCode(data.colorCode())
                        .build())
                .toList();

        categoryRepository.saveAll(defaultCategories);

        return newUser.getId();
    }

    /**
     * Lấy danh sách những người dùng có quyền hệ thống (loại trừ các User thông
     * thường).
     * Kết quả trả về sẽ được sắp xếp theo thời gian cập nhật mới nhất.
     */
    @Override
    public List<UserGetsResponse> getUser() {
        List<UserEntity> users = userRepository.findByUserRoles_Role_NameNot(RoleName.USER);

        List<UserGetsResponse> responseList = users.stream()
                .map(user -> UserGetsResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .role(user.getUserRoleEntities().stream().findFirst().map(ur -> ur.getRoleEntity().getName().name())
                                .orElse(null))
                        .avatarUrl(user.getAvatarUrl())
                        .defaultCurrency(user.getDefaultCurrency())
                        .createTime(user.getCreatedAt())
                        .lastModifiedTime(user.getUpdatedAt())
                        .build())
                .sorted(Comparator.comparing(UserGetsResponse::getLastModifiedTime,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();

        return responseList;
    }

    /**
     * Xử lý đăng xuất người dùng bằng cách vô hiệu hóa Access Token và Refresh
     * Token.
     */
    @Override
    public void logout(String accessToken, String refreshToken) {
        if (accessToken != null && !accessToken.trim().isEmpty()) {
            blacklistToken(accessToken);
        }
        if (refreshToken != null && !refreshToken.trim().isEmpty()) {
            try {
                String username = jwtTokenProvider.extractUsername(refreshToken);
                String redisKey = "refreshToken:" + username;
                redisTemplate.delete(redisKey);
                log.info("Đã xóa Refresh Token hợp lệ của user: {}", username);
            } catch (Exception e) {
                log.warn("Không thể trích xuất username từ Refresh Token hoặc lỗi khi xóa Redis: {}", e.getMessage());
            }
            blacklistToken(refreshToken);
        }
    }

    private void blacklistToken(String token) {
        try {
            long expirationSeconds = Math.max(0,
                    (jwtTokenProvider.extractExpiration(token).getTime() - System.currentTimeMillis()) / 1000);

            if (expirationSeconds > 0) {
                TokenBlackList blackListRecord = new TokenBlackList(token, expirationSeconds);
                tokenBlackListRepository.put(blackListRecord);
                log.info("Đã đưa token vào blacklist với thời gian sống (giây): {}", expirationSeconds);
            }
        } catch (Exception e) {
            log.warn("Lỗi khi đưa token vào blacklist hoặc token đã hết hạn: {}", e.getMessage());
        }
    }
}
