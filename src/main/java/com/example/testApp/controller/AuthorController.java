package com.example.testApp.controller;

import com.example.testApp.model.Author;
import com.example.testApp.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/authors")
    public List<Author> all() {
        return authorService.getAll();
    }

    @PostMapping("/authors")
    public Author add(@RequestBody Author newAuthor) {
        return authorService.add(newAuthor);
    }

    @GetMapping("/authors/{id}")
    public Author get(@PathVariable Long id) {
        return authorService.get(id);
    }

    @PutMapping("/authors/{id}")
    public Author updateOrCreate(@RequestBody Author newAuthor, @PathVariable Long id) {
        return authorService.updateOrCreate(newAuthor, id);
    }

    @DeleteMapping("/authors/{id}")
    public void deleteBook(@PathVariable Long id) {
        authorService.delete(id);
    }

}
