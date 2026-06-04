package com.example.repository;

import com.example.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Book repository interface
 * Extends JpaRepository for database operations
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Find book by ISBN
     *
     * @param isbn the ISBN of the book
     * @return Optional containing the book if found
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Find books by author
     *
     * @param author the author name
     * @return List of books by the author
     */
    java.util.List<Book> findByAuthor(String author);
}