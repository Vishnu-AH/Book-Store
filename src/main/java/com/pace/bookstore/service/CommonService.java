package com.pace.bookstore.service;

import com.pace.bookstore.dto.*;
import com.pace.bookstore.entity.*;
import com.pace.bookstore.exceptions.BookStoreException;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface CommonService {

    void signUp(SignUpDTO signUpDTO) throws BookStoreException;

    void addCategory(BookCategoryDTO bookCategoryDTO) throws BookStoreException;

    BookCategoryDTO fetchCategoryById(UUID categoryId) throws BookStoreException;


    void updateBookCategory(UUID categoryId, String name, boolean flag) throws BookStoreException;

    Page<BookCategory> getAllTheBookCategories(int pageNumber, int pageSize);

    void addBooks(BookDTO bookDto) throws BookStoreException;

    BookDTO getBookById(UUID id) throws BookStoreException;

    Page<Books> getAllTheBooks(int pageNumber, int pageSize);

    void updateBook(UUID bookId, double price) throws BookStoreException;

    void deleteBook(UUID bookId, UUID categoryId) throws BookStoreException;

    double addCart(CartRequestDTO cartRequestDTO) throws BookStoreException;

    Page<Cart> fetchCart(int pageNumber, int pageSize);

    void deleteCart(UUID cartId) throws BookStoreException;

    List<Books> findAllBookByCategory(UUID categoryId, int pageNumber, int pageSize) throws BookStoreException;

    String orderConfirmation(OrderCartDTO orderCartDTO) throws BookStoreException;

    List<Orders> findOrderByDateRange(Date startDate, Date endDate);

    Books findMostSoldBook() throws BookStoreException;

    List<User> findBookPurchases(UUID bookId) throws BookStoreException;
}
