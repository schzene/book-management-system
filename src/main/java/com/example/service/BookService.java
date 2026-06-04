package com.example.service;

import com.example.entity.Book;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Book service class
 * Handles business logic for book operations with caching
 */
@Service
@Slf4j
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    private static final String CACHE_NAME = "books";

    /**
     * Get book by ID with caching
     *
     * @param id the book ID
     * @return the book
     * @throws ResourceNotFoundException if book not found
     */
    @Cacheable(value = CACHE_NAME, key = "#id")
    public Book getBookById(Long id) {
        log.info("Fetching book with id: {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    /**
     * Get all books
     *
     * @return list of all books
     */
    @Cacheable(value = CACHE_NAME, key = "'all'")
    public List<Book> getAllBooks() {
        log.info("Fetching all books");
        return bookRepository.findAll();
    }

    /**
     * Find books by author
     *
     * @param author the author name
     * @return list of books by author
     */
    @Cacheable(value = CACHE_NAME, key = "'author_' + #author")
    public List<Book> getBooksByAuthor(String author) {
        log.info("Fetching books by author: {}", author);
        return bookRepository.findByAuthor(author);
    }

    /**
     * Find book by ISBN
     *
     * @param isbn the ISBN
     * @return the book
     */
    @Cacheable(value = CACHE_NAME, key = "'isbn_' + #isbn")
    public Optional<Book> getBookByIsbn(String isbn) {
        log.info("Fetching book by ISBN: {}", isbn);
        return bookRepository.findByIsbn(isbn);
    }

    /**
     * Create new book
     *
     * @param book the book to create
     * @return the created book
     */
    @CachePut(value = CACHE_NAME, key = "#result.id")
    public Book createBook(Book book) {
        log.info("Creating new book: {}", book.getBookName());
        return bookRepository.save(book);
    }

    /**
     * Update book
     *
     * @param id the book ID
     * @param bookDetails the updated book details
     * @return the updated book
     * @throws ResourceNotFoundException if book not found
     */
    @CachePut(value = CACHE_NAME, key = "#id")
    public Book updateBook(Long id, Book bookDetails) {
        log.info("Updating book with id: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        if (bookDetails.getBookName() != null) {
            book.setBookName(bookDetails.getBookName());
        }
        if (bookDetails.getAuthor() != null) {
            book.setAuthor(bookDetails.getAuthor());
        }
        if (bookDetails.getPrice() != null) {
            book.setPrice(bookDetails.getPrice());
        }
        if (bookDetails.getIsbn() != null) {
            book.setIsbn(bookDetails.getIsbn());
        }
        if (bookDetails.getPublishTime() != null) {
            book.setPublishTime(bookDetails.getPublishTime());
        }

        return bookRepository.save(book);
    }

    /**
     * Delete book
     *
     * @param id the book ID
     * @throws ResourceNotFoundException if book not found
     */
    @CacheEvict(value = CACHE_NAME, key = "#id")
    public void deleteBook(Long id) {
        log.info("Deleting book with id: {}", id);
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    /**
     * Clear all book cache
     */
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void clearCache() {
        log.info("Clearing all book cache");
    }
}