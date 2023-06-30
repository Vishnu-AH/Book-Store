package com.pace.bookstore.repository;

import com.pace.bookstore.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    @Query("select o from Role o where o.name=:role")
    Optional<Role> findRoleByName(String role);
}
