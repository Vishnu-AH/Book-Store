package com.pace.bookstore.repository;

import com.pace.bookstore.entity.Role;
import com.pace.bookstore.entity.UserRoleXref;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserRoleXrefRepository extends JpaRepository<UserRoleXref, UUID> {
    @Query("select UserRoleXref from UserRoleXref o where o.user.id=:id")
    List<UserRoleXref> findByUserId(UUID id);

    @Query("select o.role from UserRoleXref o where o.user.id=:id")
    List<Role> findUserRoleByUserId(UUID id);

    @Query("select o.role from UserRoleXref o where o.user.id=:id")
    Role findRoleByUserId(UUID id);
}