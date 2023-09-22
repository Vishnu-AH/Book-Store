package com.pace.bookstore.repository;

import com.pace.bookstore.entity.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Books, UUID> {
    @Query("select o from Books o where o.authorName=:authorName and o.bookName=:bookName")
    Optional<Books> findByAuthorName(String authorName, String bookName);
}