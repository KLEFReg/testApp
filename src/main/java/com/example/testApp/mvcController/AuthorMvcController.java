package com.example.testApp.mvcController;

import com.example.testApp.model.Author;
import com.example.testApp.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthorMvcController {

    @Autowired
    private AuthorService authorService;

    @Value("${error.message}")
    private String errorMessage;

    private static List<Author> authors = new ArrayList<Author>();

    @GetMapping("/authorList")
    public String bookList(Model model) {
        authors.clear();
        authors.addAll(authorService.getAll());

        model.addAttribute("authors", authors);

        return "author/authorList";
    }

    @GetMapping("/addAuthor")
    public String showAddAuthorPage(Model model) {
        var author = new Author();
        model.addAttribute("author", author);

        return "author/addAuthor";
    }

    @PostMapping("/addAuthor")
    public String addBook(Model model, @ModelAttribute("author") Author author) throws IOException {

        String name = author.getName();

        if (name != null && !name.isEmpty()) {
            var newAuthor = authorService.add(author);
            model.addAttribute("author", newAuthor);
            return "redirect:/updateAuthor/"+newAuthor.getId();
        }

        model.addAttribute("errorMessage", errorMessage);
        return "redirect:/authorList";
    }

    @PostMapping("/saveAuthor")
    public String saveBook(Model model, @ModelAttribute("author") Author author) throws IOException {

        String name = author.getName();

        if (name != null && !name.isEmpty()) {
            var id = author.getId();
            authorService.updateOrCreate(author, id);
            model.addAttribute("author", author);
            return "redirect:/updateAuthor/"+ id;
        }

        model.addAttribute("errorMessage", errorMessage);
        return "redirect:/";
    }

    @GetMapping("/updateAuthor/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") Long id, Model model) {

        var entity = authorService.get(id);

        model.addAttribute("author", entity);
        return "author/updateAuthor";
    }

    @GetMapping("/deleteAuthor/{id}")
    public String deleteEmployee(@PathVariable(value = "id") Long id) {

        this.authorService.delete(id);
        return "redirect:/authorList";
    }

}
