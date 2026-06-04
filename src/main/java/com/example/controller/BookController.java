package com.example.controller;

import com.example.entity.Book;
import com.example.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Book controller class
 * Handles HTTP requests for book operations
 */
@RestController
@RequestMapping("/books")
@Validated
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * Get book by ID
     *
     * @param id the book ID
     * @return ResponseEntity with the book
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable @NotNull(message = "ID cannot be null") Long id) {
        log.info("GET /books/{}", id);
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    /**
     * Get all books
     *
     * @return ResponseEntity with list of all books
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        log.info("GET /books - Fetching all books");
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    /**
     * Get books by author
     *
     * @param author the author name
     * @return ResponseEntity with list of books by author
     */
    @GetMapping("/author/{author}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable String author) {
        log.info("GET /books/author/{}", author);
        List<Book> books = bookService.getBooksByAuthor(author);
        return ResponseEntity.ok(books);
    }

    /**
     * Get book by ISBN
     *
     * @param isbn the ISBN
     * @return ResponseEntity with the book
     */
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<?> getBookByIsbn(@PathVariable String isbn) {
        log.info("GET /books/isbn/{}", isbn);
        Optional<Book> book = bookService.getBookByIsbn(isbn);
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create new book
     *
     * @param book the book to create
     * @return ResponseEntity with the created book
     */
    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        log.info("POST /books - Creating new book: {}", book.getBookName());
        Book createdBook = bookService.createBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    /**
     * Update book
     *
     * @param id the book ID
     * @param book the updated book details
     * @return ResponseEntity with the updated book
     */
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable @NotNull(message = "ID cannot be null") Long id,
            @Valid @RequestBody Book book) {
        log.info("PUT /books/{} - Updating book", id);
        Book updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Delete book
     *
     * @param id the book ID
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable @NotNull(message = "ID cannot be null") Long id) {
        log.info("DELETE /books/{}", id);
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Clear cache
     *
     * @return ResponseEntity with success message
     */
    @PostMapping("/cache/clear")
    public ResponseEntity<String> clearCache() {
        log.info("POST /books/cache/clear - Clearing cache");
        bookService.clearCache();
        return ResponseEntity.ok("Cache cleared successfully");
    }
}