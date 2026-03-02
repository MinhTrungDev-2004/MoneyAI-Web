package com.datn.moneyai.repositories;

import com.datn.moneyai.models.entities.bases.User;
import com.datn.moneyai.models.entities.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findByUserRoles_Role_NameNot(RoleName role);
}
