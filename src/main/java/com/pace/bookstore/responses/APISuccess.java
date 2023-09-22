package com.pace.bookstore.responses;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class APISuccess {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private Object data;

    public APISuccess() {
        timestamp = LocalDateTime.now();
    }

    public APISuccess(HttpStatus status) {
        this();
        this.status = status;
    }

    public APISuccess(HttpStatus status, Object object) {
        this();
        this.status = status;
        this.data = object;
    }
}
