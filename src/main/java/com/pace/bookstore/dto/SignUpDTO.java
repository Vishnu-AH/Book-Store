package com.pace.bookstore.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SignUpDTO {
    private String name;

    private String email;

    private long phoneNumber;

    private String password;

    private String role;

    private boolean isActive;
}