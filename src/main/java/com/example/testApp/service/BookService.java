
package com.example.testApp.service;

import com.example.testApp.exception.BadResourceException;
import com.example.testApp.exception.ResourceAlreadyExistsException;
import com.example.testApp.exception.ResourceNotFoundException;
import com.example.testApp.model.Book;
import com.example.testApp.repository.BookRepository;
import com.example.testApp.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private FileRepository fileRepository;

    private boolean existsById(Long id) {
        return bookRepository.existsById(id);
    }

    public Book findById(Long id) throws ResourceNotFoundException {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            throw new ResourceNotFoundException("Cannot find Book with id: " + id);
        } else return book;
    }

    public List<Book> findAll(Integer pageNumber, Integer rowPerPage) {
        List<Book> books = new ArrayList<>();
        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage,
                Sort.by("id").ascending());
        bookRepository.findAll(sortedByIdAsc).forEach(books::add);
        return books;
    }

    public List<Book> searchBooks(String word, Integer pageNumber, Integer rowPerPage) {
        if (!word.startsWith("%")) word = "%" + word;
        if (!word.endsWith("%")) word = word + "%";

        List<Book> books = new ArrayList<>();
        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage,
                Sort.by("id").ascending());
        bookRepository.findAllByTitleLikeOrDescriptionLikeOrAuthors_NameLikeOrGenres_NameLikeAllIgnoreCase(word, word, word, word, sortedByIdAsc).forEach(books::add);
        return books;
    }

    public List<Book> randomBooks(Long count) {
        List<Book> books = new ArrayList<>();
        bookRepository.random(count).forEach(books::add);
        return books;
    }

    @Transactional
    public Book save(Book book) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(book.getTitle())) {
            if (book.getId() != null && existsById(book.getId())) {
                throw new ResourceAlreadyExistsException("Book with id: " + book.getId() +
                        " already exists");
            }

            if (book.getCover() != null) {
                var cover = fileRepository.save(book.getCover());
                if (cover != null)
                    book.setCover(cover);
            }
            if (book.getContent() != null) {
                var content = fileRepository.save(book.getContent());
                if (content != null)
                    book.setContent(content);
            }

            return bookRepository.save(book);
        } else {
            BadResourceException exc = new BadResourceException("Failed to save book");
            exc.addErrorMessage("Book is null or empty");
            throw exc;
        }
    }

    public void update(Book book)
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(book.getTitle())) {
            if (!existsById(book.getId())) {
                throw new ResourceNotFoundException("Cannot find Book with id: " + book.getId());
            }
            bookRepository.save(book);
        } else {
            BadResourceException exc = new BadResourceException("Failed to save book");
            exc.addErrorMessage("Book is null or empty");
            throw exc;
        }
    }

    public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Cannot find book with id: " + id);
        } else {
            bookRepository.deleteById(id);
        }
    }

    public Long count() {
        return bookRepository.count();
    }

}

