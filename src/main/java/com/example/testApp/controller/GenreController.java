package com.example.testApp.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.example.testApp.exception.BadResourceException;
import com.example.testApp.exception.ResourceAlreadyExistsException;
import com.example.testApp.exception.ResourceNotFoundException;
import com.example.testApp.model.Genre;
import com.example.testApp.service.GenreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class GenreController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final int ROW_PER_PAGE = 5;

    @Autowired
    private GenreService genreService;

    @GetMapping(value = "/genres", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Genre>> findAll(
            @RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(genreService.findAll(pageNumber, ROW_PER_PAGE));
    }

    @GetMapping(value = "/genres/{genreId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Genre> findGenreById(@PathVariable long genreId) {
        try {
            Genre book = genreService.findById(genreId);
            return ResponseEntity.ok(book);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping(value = "/genres")
    public ResponseEntity<Genre> addGenre( @RequestBody Genre genre)
            throws URISyntaxException {
        try {
            Genre newGenre = genreService.save(genre);
            return ResponseEntity.created(new URI("/api/genres/" + newGenre.getId()))
                    .body(genre);
        } catch (ResourceAlreadyExistsException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (BadResourceException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping(value = "/genres/{genreId}")
    public ResponseEntity<Genre> updateGenre(@RequestBody Genre genre,
                                             @PathVariable long genreId) {
        try {
            genre.setId(genreId);
            genreService.update(genre);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (BadResourceException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/genres/{genreId}")
    public ResponseEntity<Void> deleteGenreById(@PathVariable long genreId) {
        try {
            genreService.deleteById(genreId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
