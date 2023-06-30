package com.pace.bookstore.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDTO {

    private String name;

    private String email;

    private long phoneNumber;

}