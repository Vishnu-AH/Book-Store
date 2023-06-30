package com.pace.bookstore.controller;

import com.pace.bookstore.dto.*;
import com.pace.bookstore.entity.BookCategory;
import com.pace.bookstore.entity.Books;
import com.pace.bookstore.entity.Orders;
import com.pace.bookstore.entity.User;
import com.pace.bookstore.exceptions.BookStoreException;
import com.pace.bookstore.responses.APISuccess;
import com.pace.bookstore.service.CommonService;
import com.pace.bookstore.utility.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookstore")
public class CommonController {
    private final CommonService commonService;

    @PostMapping("/public/signup")
    public ResponseEntity<Object> signUp(@RequestBody SignUpDTO signUpDTO) throws BookStoreException {
        commonService.signUp(signUpDTO);
        return CommonUtils.buildResponse(new APISuccess(HttpStatus.OK, "User Sign up successfull"));
    }

    @PostMapping("/public/category")
    public ResponseEntity<Object> addCategory(@RequestBody BookCategoryDTO bookCategoryDTO) throws BookStoreException {
        commonService.addCategory(bookCategoryDTO);
        return CommonUtils.buildResponse(new APISuccess(HttpStatus.OK, "Category added successfully"));
    }

    @GetMapping("/public/category")
    public ResponseEntity<Object> fetchCategoryById(@RequestParam UUID categoryId) throws BookStoreException {
        BookCategoryDTO bookCategoryDTO = commonService.fetchCategoryById(categoryId);
        return CommonUtils.buildResponse(new APISuccess(HttpStatus.OK, bookCategoryDTO));
    }

    @GetMapping("/public/fetchallbookCategories")
    public ResponseEntity<Page<BookCategory>> getAllTheBookCategories(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Page<BookCategory> bookPage = commonService.getAllTheBookCategories(pageNumber, pageSize);
        return ResponseEntity.ok(bookPage);
    }

    @PutMapping("/public/category")
    public ResponseEntity<Object> updateBookCategory(@RequestParam UUID categoryId, @RequestParam String newName, boolean flag) throws BookStoreException {
        commonService.updateBookCategory(categoryId, newName, flag);
        return CommonUtils.buildResponse(new APISuccess(HttpStatus.OK, "Category updated successfully"));
    }


    @PostMapping("/public/books")
    public ResponseEntity<Object> addBooks(@RequestBody BookDTO bookDto) throws BookStoreException {
        commonService.addBooks(bookDto);
        return CommonUtils.buildResponse(new APISuccess(HttpStatus.OK, "Book added successfully"));
    }

    @GetMapping("/public/books")
    public ResponseEntity<Object> getBookById(@RequestParam UUID id) throws BookStoreException {
        BookDTO book = commonService.getBookById(id);
        return CommonUtils.buildResponse(new APISuccess(HttpStatus.OK, book));
    }

    @GetMapping("/public/fetchallbooks")
    public ResponseEntity<Page<Books>> getAllTheBooks(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Page<Books> bookPage = commonService.getAllTheBooks(pageNumber, pageSize);
        return ResponseEntity.ok(bookPage);
    }

    @PutMapping("/public/books")
    public ResponseEntity<Object> updateBook(@RequestParam UUID bookId, @RequestParam double price) {
        commonService.updateBook(bookId, price);
        return CommonUtils.buildResponse(new APISuccess(HttpStatus.OK, "Book updated successfully"));
    }

    @DeleteMapping("/public/books")
    public ResponseEntity<Object> deleteBook(@RequestParam UUID bookId, @RequestParam UUID categoryId) throws BookStoreException {
        commonService.deleteBook(bookId, categoryId);
        return CommonUtils.buildResponse(new APISuccess(HttpStatus.OK, "Book Deleted successfully"));
    }

    @GetMapping("/public/books/category")
    public ResponseEntity<Object> findAllBookByCategory(@RequestParam UUID categoryId, @RequestParam int pageNumber, @RequestParam int pageSize) throws BookStoreException {
        List<Books> books = commonService.findAllBookByCategory(categoryId, pageNumber, pageSize);
        return CommonUtils.buildResponse(new APISuccess(HttpStatus.OK, books));
    }

    @PostMapping("/public/cart")
    public ResponseEntity<Object> addCart(@RequestBody CartRequestDTO cartRequestDTO) throws BookStoreException {
        double amount = commonService.addCart(cartRequestDTO);
        return CommonUtils.buildResponse(new APISuccess(HttpStatus.OK, "Item added to cart. " + " Click here for payment and confirm order. Your payable amount is -> " + amount + " /bookstore/public/payment/orderConfirm"));
    }

    @GetMapping("/public/fetchallcart")
    public ResponseEntity<Object> getAllCart(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Page<Books> bookPage = commonService.getAllTheBooks(pageNumber, pageSize);
        return CommonUtils.buildResponse(new APISuccess(HttpStatus.OK,bookPage));

    }

    @DeleteMapping("/public/cart")
    public ResponseEntity<Object> deleteCart(@RequestParam UUID cartId) throws BookStoreException {
        commonService.deleteCart(cartId);
        return CommonUtils.buildResponse(new APISuccess(HttpStatus.OK, "Cart deleted successfully"));
    }

    @PostMapping("/public/payment/orderConfirm")
    public ResponseEntity<Object> orderConfirmation(@RequestBody OrderCartDTO orderCartDTO) throws BookStoreException {
        String response = commonService.orderConfirmation(orderCartDTO);
        return CommonUtils.buildResponse(new APISuccess(HttpStatus.OK, response));
    }

    @GetMapping("/public/orders/date")
    public ResponseEntity<Object> findOrderByDateRange(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) Date startDate, @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) Date endDate) {
        List<Orders> orderList = commonService.findOrderByDateRange(startDate, endDate);
        return CommonUtils.buildResponse(new APISuccess(HttpStatus.OK, orderList));
    }

    @GetMapping("/public/orders/mostSold")
    public ResponseEntity<Object> findMostSoldBook() throws BookStoreException {
        Books books = commonService.findMostSoldBook();
        return CommonUtils.buildResponse(new APISuccess(HttpStatus.OK, books));
    }

    @GetMapping("/public/user/book/purchases")
    public ResponseEntity<Object> findBookPurchases(@RequestParam UUID bookId) throws BookStoreException {
        List<User> userList = commonService.findBookPurchases(bookId);
        return CommonUtils.buildResponse(new APISuccess(HttpStatus.OK, userList));
    }

}
