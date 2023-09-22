package com.pace.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pace.bookstore.utility.AuditModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookCategory extends AuditModel {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @Column(name = "category_name", unique = true, nullable = false)
    private String categoryName;

    @Column(name = "is_active")
    private boolean isActive;
}
