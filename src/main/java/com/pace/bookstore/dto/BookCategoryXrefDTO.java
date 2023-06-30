package com.pace.bookstore.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class BookCategoryXrefDTO {

    private UUID bookId;

    private UUID categoryId;

}