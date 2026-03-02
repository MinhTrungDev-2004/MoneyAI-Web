package com.datn.moneyai.models.entities;

import com.datn.moneyai.models.entities.bases.BaseEntity;
import com.datn.moneyai.models.entities.enums.AuthProvider;
import com.datn.moneyai.models.entities.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private String fullName;

    private String avatarUrl;

    private String defaultCurrency = "VND";

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Boolean isActive = true;
}