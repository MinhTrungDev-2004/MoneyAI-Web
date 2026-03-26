package com.datn.moneyai.repositories;

import com.datn.moneyai.models.entities.bases.RoleEntity;
import com.datn.moneyai.models.entities.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    @Query(value = "SELECT * FROM roles WHERE name = :#{#name.name()}", nativeQuery = true)
    Optional<RoleEntity> findByName(@Param("name") RoleName name);
}