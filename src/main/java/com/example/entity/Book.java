package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Book entity class
 * Represents a book record in the database
 */
@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_name", length = 100, nullable = false)
    @NotBlank(message = "Book name cannot be blank")
    @Size(min = 1, max = 100, message = "Book name must be between 1 and 100 characters")
    private String bookName;

    @Column(name = "author", length = 50)
    @Size(max = 50, message = "Author name must not exceed 50 characters")
    private String author;

    @Column(name = "price")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Price must not exceed 999999.99")
    private BigDecimal price;

    @Column(name = "ISBN", length = 20, unique = true)
    private String isbn;

    @Column(name = "publish_time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishTime;
}