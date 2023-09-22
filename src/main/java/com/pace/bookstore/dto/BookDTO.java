package com.pace.bookstore.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BookDTO {

    private String bookName;

    private String authorName;

    private double price;

    private UUID categoryId;

    private UUID userId;

    private boolean isActive;
}
