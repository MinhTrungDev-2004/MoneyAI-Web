package com.datn.moneyai.repositories;

import com.datn.moneyai.models.entities.bases.Role;
import com.datn.moneyai.models.entities.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query(value = "SELECT * FROM roles WHERE name = :#{#name.name()}", nativeQuery = true)
    Optional<Role> findByName(@Param("name") RoleName name);
}