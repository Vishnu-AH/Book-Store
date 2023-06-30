package com.pace.bookstore.repository;


import com.pace.bookstore.entity.Books;
import com.pace.bookstore.entity.BooksSold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BooksSoldRepository extends JpaRepository<BooksSold, UUID> {

    @Query("select o from BooksSold o where o.books.id=:bookId")
    Optional<BooksSold> findByBookId(UUID bookId);

    @Query("SELECT o.books FROM Cart o ORDER BY o.quantity DESC")
    List<Books> findMostSoldBook();
}