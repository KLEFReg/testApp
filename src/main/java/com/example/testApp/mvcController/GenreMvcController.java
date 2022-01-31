package com.example.testApp.mvcController;

import com.example.testApp.exception.ResourceNotFoundException;
import com.example.testApp.model.Genre;
import com.example.testApp.service.GenreService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GenreMvcController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final int ROW_PER_PAGE = 5;

    @Autowired
    private GenreService genreService;

    @GetMapping(value = "/genres")
    public String getGenres(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<Genre> genres = genreService.findAll(pageNumber, ROW_PER_PAGE);

        long count = genreService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("genres", genres);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);
        return "genre/genreList";
    }

    @GetMapping(value = "/genres/{genreId}")
    public String getGenreById(Model model, @PathVariable long genreId) {
        Genre genre = null;
        try {
            genre = genreService.findById(genreId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Genre not found");
        }
        model.addAttribute("genre", genre);
        return "genre/genre";
    }

    @GetMapping(value = {"/genres/add"})
    public String showAddGenre(Model model) {
        Genre genre = new Genre();
        model.addAttribute("add", true);
        model.addAttribute("genre", genre);

        return "genre/genreEdit";
    }

    @PostMapping(value = "/genres/add")
    public String addGenre(Model model, @ModelAttribute("genre") Genre genre) {
        try {
            Genre newGenre = genreService.save(genre);
            return "redirect:/genres";
        } catch (Exception ex) {

            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            model.addAttribute("add", true);
            return "genre/genreEdit";
        }
    }

    @GetMapping(value = {"/genres/{genreId}/edit"})
    public String showEditGenre(Model model, @PathVariable long genreId) {
        Genre genre = null;
        try {
            genre = genreService.findById(genreId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Genre not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("genre", genre);
        return "genre/genreEdit";
    }

    @PostMapping(value = {"/genres/{genreId}/edit"})
    public String updateGenre(Model model, @PathVariable long genreId, @ModelAttribute("genre") Genre genre) {
        try {
            genre.setId(genreId);
            genreService.update(genre);
            return "redirect:/genres/" + String.valueOf(genre.getId());
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            model.addAttribute("add", false);
            return "genre/genreEdit";
        }
    }

    @GetMapping(value = {"/genres/{genreId}/delete"})
    public String showDeleteGenreById(Model model, @PathVariable long genreId) {
        Genre genre = null;
        try {
            genre = genreService.findById(genreId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Genre not found");
        }
        model.addAttribute("allowDelete", true);
        model.addAttribute("genre", genre);
        return "genre/genre";
    }

    @PostMapping(value = {"/genres/{genreId}/delete"})
    public String deleteGenreById(Model model, @PathVariable long genreId) {
        try {
            genreService.deleteById(genreId);
            return "redirect:/genres";
        } catch (ResourceNotFoundException ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "genre/genre";
        }
    }
}
