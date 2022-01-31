
package com.example.testApp.service;

import com.example.testApp.model.Author;
import com.example.testApp.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    public Author add(Author newAuthor) {
        return authorRepository.save(newAuthor);
    }

    public Author get(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Could not find author " + id));
    }

    public Author updateOrCreate(Author newAuthor, Long id) {
        return authorRepository.findById(id)
                .map(author -> {
                    author.setName(newAuthor.getName());
                    return authorRepository.save(author);
                })
                .orElseGet(() -> {
                    newAuthor.setId(id);
                    return authorRepository.save(newAuthor);
                });
    }

    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
}

