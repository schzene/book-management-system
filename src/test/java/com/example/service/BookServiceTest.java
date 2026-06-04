package com.example.service;

import com.example.entity.Book;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookService
 */
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId(1L);
        testBook.setBookName("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setPrice(new BigDecimal("29.99"));
        testBook.setIsbn("978-0-123456-78-9");
        testBook.setPublishTime(LocalDate.of(2023, 1, 1));
    }

    @Test
    void testGetBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        Book result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(testBook.getId(), result.getId());
        assertEquals(testBook.getBookName(), result.getBookName());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBookByIdNotFound() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById(999L);
        });
        verify(bookRepository, times(1)).findById(999L);
    }

    @Test
    void testCreateBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        Book result = bookService.createBook(testBook);

        assertNotNull(result);
        assertEquals(testBook.getBookName(), result.getBookName());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testUpdateBook() {
        Book updatedBook = new Book();
        updatedBook.setBookName("Updated Book");
        updatedBook.setAuthor("Updated Author");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        Book result = bookService.updateBook(1L, updatedBook);

        assertNotNull(result);
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testDeleteBook() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> bookService.deleteBook(1L));
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBookNotFound() {
        when(bookRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.deleteBook(999L);
        });
    }
}