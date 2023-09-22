package com.pace.bookstore.repository;

import com.pace.bookstore.entity.BookCategory;
import com.pace.bookstore.entity.BookCategoryXref;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CategoriesRepository extends JpaRepository<BookCategory, UUID> {
    @Query("select o from BookCategory o where o.categoryName=:categoryName")
    Optional<BookCategory> findByCategoryName(String categoryName);

    @Query("select o from BookCategoryXref o where o.books.id=:bookId and o.category.id=:categoryId")
    Optional<BookCategoryXref> findByBookIdCategoryId(UUID bookId, UUID categoryId);
}
