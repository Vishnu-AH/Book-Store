package com.pace.bookstore.repository;

import com.pace.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("select o from User o where o.email= :email")
    Optional<User> findByEmail(String email);


}