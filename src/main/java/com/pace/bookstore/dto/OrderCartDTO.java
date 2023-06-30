package com.pace.bookstore.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderCartDTO {

    private UUID userId;

    private UUID bookId;

    private double amount;

    private String address;

}