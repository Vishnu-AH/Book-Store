package com.pace.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pace.bookstore.utility.AuditModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class BookCategoryXref extends AuditModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id")
    private Books books;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categories_id")
    private BookCategory category;

    @Column(name = "is_active")
    private boolean isActive;
}