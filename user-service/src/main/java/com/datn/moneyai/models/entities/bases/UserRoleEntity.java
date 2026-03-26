package com.datn.moneyai.models.entities.bases;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "user_roles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity roleEntity;
}
