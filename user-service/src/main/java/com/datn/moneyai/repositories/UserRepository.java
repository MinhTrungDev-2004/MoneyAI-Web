package com.datn.moneyai.repositories;

import com.datn.moneyai.models.entities.bases.User;
import com.datn.moneyai.models.entities.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT CASE WHEN COUNT(u.id) > 0 THEN 1 ELSE 0 END FROM user u WHERE u.email = :email", nativeQuery = true)
    boolean existsByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM user WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);

    @Query(value = "SELECT u.* FROM user u JOIN user_roles ur ON u.id = ur.user_id JOIN roles r ON ur.role_id = r.id WHERE r.name != :#{#role.name()}", nativeQuery = true)
    List<User> findByUserRoles_Role_NameNot(@Param("role") RoleName role);
}
