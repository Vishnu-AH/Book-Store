//package com.pace.bookstore.exceptions;
//
//import com.pace.bookstore.responses.ApiError;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//@RestControllerAdvice
//public class Exception extends ResponseEntityExceptionHandler {
//    @ExceptionHandler
//    public ResponseEntity<Object> bookNotFoundExceptionHandler(BookStoreException ex) {
//        ApiError response = new ApiError();
//        response.setMessage(ex.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//    }
//}