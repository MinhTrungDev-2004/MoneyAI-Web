package com.datn.moneyai.models.entities.bases;

import com.datn.moneyai.models.entities.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true)
    private RoleName name;
}
