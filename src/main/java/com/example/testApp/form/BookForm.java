package com.example.testApp.form;

import com.example.testApp.model.Author;
import com.example.testApp.model.Book;
import com.example.testApp.model.File;
import com.example.testApp.model.Genre;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.Set;

public class BookForm {
    private Long id;
    private String title;
    private String description;
    private Set<Author> authors;
    private Set<Genre> genres;
    private File cover;
    private MultipartFile coverFile;
    private File content;
    private MultipartFile contentFile;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public File getCover() {
        return cover;
    }

    public void setCover(File cover) {
        this.cover = cover;
    }

    public MultipartFile getCoverFile() {
        return coverFile;
    }

    public void setCoverFile(MultipartFile coverFile) throws IOException {
        this.coverFile = coverFile;
        if (coverFile != null) {
            if (this.cover == null) this.cover = new File();
            this.cover.setName(coverFile.getOriginalFilename());
            this.cover.setContent(coverFile.getBytes());
        }
    }

    public File getContent() {
        return content;
    }

    public void setContent(File content) {
        this.content = content;
    }

    public MultipartFile getContentFile() {
        return contentFile;
    }

    public void setContentFile(MultipartFile contentFile) throws IOException {
        this.contentFile = contentFile;
        if (contentFile != null) {
            if (this.content == null) this.content = new File();
            this.content.setName(contentFile.getOriginalFilename());
            this.content.setContent(contentFile.getBytes());
        }
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public BookForm() {
    }

    public BookForm(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.description = book.getDescription();
        this.authors = book.getAuthors();
        this.genres = book.getGenres();
        this.cover = book.getCover();
        this.content = book.getContent();
    }

}
