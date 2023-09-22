package com.pace.bookstore.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CartRequestDTO {
    private UUID user;
    private UUID books;
    private int quantity;

}
