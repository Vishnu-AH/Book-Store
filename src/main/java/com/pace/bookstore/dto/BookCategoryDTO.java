package com.pace.bookstore.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookCategoryDTO {

    private String categoryName;

    private boolean isActive;

}