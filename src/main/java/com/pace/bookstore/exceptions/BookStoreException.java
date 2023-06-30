package com.pace.bookstore.exceptions;

import lombok.Data;

@Data
public class BookStoreException extends RuntimeException {

    private String message;

    public BookStoreException(String message) {
        this.message = message;
    }

}