package com.datn.moneyai.repositories;

import com.datn.moneyai.models.entities.bases.Role;
import com.datn.moneyai.models.entities.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);
}