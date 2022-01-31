package com.example.testApp.controller;

import com.example.testApp.exception.BadResourceException;
import com.example.testApp.exception.ResourceAlreadyExistsException;
import com.example.testApp.exception.ResourceNotFoundException;
import com.example.testApp.model.Book;
import com.example.testApp.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final int ROW_PER_PAGE = 5;

    @Autowired
    private BookService bookService;

    @GetMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> findAll(
            @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        return ResponseEntity.ok(bookService.findAll(pageNumber, ROW_PER_PAGE));
    }

    @GetMapping(value = "/books/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> findBookById(@PathVariable long bookId) {
        try {
            Book book = bookService.findById(bookId);
            return ResponseEntity.ok(book);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping(value = "/books")
    public ResponseEntity<Book> addBook( @RequestBody Book book)
            throws URISyntaxException {
        try {
            Book newBook = bookService.save(book);
            return ResponseEntity.created(new URI("/api/books/" + newBook.getId()))
                    .body(book);
        } catch (ResourceAlreadyExistsException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (BadResourceException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping(value = "/books/{bookId}")
    public ResponseEntity<Book> updateBook(@RequestBody Book book,
                                             @PathVariable long bookId) {
        try {
            book.setId(bookId);
            bookService.update(book);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (BadResourceException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/books/{bookId}")
    public ResponseEntity<Void> deleteBookById(@PathVariable long bookId) {
        try {
            bookService.deleteById(bookId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/booksSearch/{word}")
    public List<Book> get(@PathVariable String word, @RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "row", defaultValue = "5") int rowPerPage) {
        return bookService.searchBooks(word, pageNumber, rowPerPage);
    }

}
