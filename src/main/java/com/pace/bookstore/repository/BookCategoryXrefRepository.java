package com.pace.bookstore.repository;

import com.pace.bookstore.entity.BookCategoryXref;
import com.pace.bookstore.entity.Books;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BookCategoryXrefRepository extends JpaRepository<BookCategoryXref, UUID> {

    @Query("select o.books from BookCategoryXref o where o.category.id=:categoryId")
    List<Books> findAllBooksByCategoryId(Pageable pageable, UUID categoryId);

    @Query("select o.books from BookCategoryXref o where o.category.id=:categoryId and o.books.bookName=:bookName")
    List<Books> findBooksByCategoryId(UUID categoryId, String bookName);

}