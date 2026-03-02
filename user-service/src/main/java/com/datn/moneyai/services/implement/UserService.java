package com.datn.moneyai.services.implement;

import com.datn.moneyai.exceptions.UserMessageException;
import com.datn.moneyai.models.dtos.users.UserCreateRequest;
import com.datn.moneyai.models.dtos.users.UserGetsResponse;
import com.datn.moneyai.models.entities.bases.Role;
import com.datn.moneyai.models.entities.bases.User;
import com.datn.moneyai.models.entities.bases.UserRole;
import com.datn.moneyai.models.entities.enums.RoleName;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.repositories.RoleRepository;
import com.datn.moneyai.repositories.UserRepository;
import com.datn.moneyai.services.interfaces.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public ApiResult<List<UserGetsResponse>> getUser() {
        List<User> users = userRepository.findByUserRoles_Role_NameNot(RoleName.USER);

        List<UserGetsResponse> responseList = users.stream()
                .map(user -> UserGetsResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .role(user.getUserRoles().stream().findFirst().map(ur -> ur.getRole().getName().name())
                                .orElse(null))
                        .createTime(user.getCreatedAt())
                        .lastModifiedTime(user.getUpdatedAt())
                        .build())
                .sorted(Comparator.comparing(UserGetsResponse::getLastModifiedTime,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();

        return ApiResult.success(responseList, "Lấy danh sách người dùng thành công.");
    }

    @Override
    public ApiResult<Long> createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserMessageException("Email đã tồn tại.");
        }
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // Convert role từ String sang UserRole enum
        RoleName roleName;
        try {
            roleName = RoleName.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UserMessageException("Vai trò không hợp lệ. Vui lòng chọn USER hoặc ADMIN");
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new UserMessageException("Vai trò không hợp lệ."));

        User newUser = User.builder()
                .email(request.getEmail())
                .password(hashedPassword)
                .isActive(true)
                .build();

        UserRole userRole = UserRole.builder()
                .user(newUser)
                .role(role)
                .build();
        newUser.setUserRoles(Set.of(userRole));

        userRepository.save(newUser);

        return ApiResult.success(newUser.getId(), "Đăng ký thành công.");
    }
}
