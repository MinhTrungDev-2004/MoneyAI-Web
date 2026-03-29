package com.datn.moneyai.repositories;

import com.datn.moneyai.models.entities.bases.UserEntity;
import com.datn.moneyai.models.entities.enums.RoleName;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    @EntityGraph(attributePaths = {"userRoleEntities", "userRoleEntities.roleEntity"})
    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    Optional<UserEntity> findByEmailWithRoles(@Param("email") String email);

    @EntityGraph(attributePaths = {"userRoleEntities", "userRoleEntities.roleEntity"})
    @Query("SELECT u FROM UserEntity u WHERE u.id = :id")
    Optional<UserEntity> findByIdWithRoles(@Param("id") Long id);

    @EntityGraph(attributePaths = {"userRoleEntities", "userRoleEntities.roleEntity"})
    @Query("SELECT DISTINCT u FROM UserEntity u JOIN u.userRoleEntities ur JOIN ur.roleEntity r WHERE r.name <> :role")
    List<UserEntity> findByUserRoles_Role_NameNot(@Param("role") RoleName role);
}
