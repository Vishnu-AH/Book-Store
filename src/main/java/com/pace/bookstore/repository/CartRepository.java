package com.pace.bookstore.repository;


import com.pace.bookstore.entity.Books;
import com.pace.bookstore.entity.Cart;
import com.pace.bookstore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

    @Query("SELECT o FROM Cart o WHERE o.user.id=:userId AND o.books.id=:bookId")
    Optional<Cart> findCartByUserIdBookId(UUID userId, UUID bookId);

    @Query("SELECT o FROM Cart o WHERE o.user.id=:userId")
    List<Cart> findCartByUserId(UUID userId);

    @Query("select o.user from Cart o where o.user.id=:userId")
    Optional<User> findUserByUserId(UUID userId);

    @Query("select o.books from Cart o where o.books.id=:bookId")
    Optional<Books> findCartByBookId(UUID bookId);

}

