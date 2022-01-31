package com.example.testApp.mvcController;

import com.example.testApp.exception.ResourceNotFoundException;
import com.example.testApp.form.BookForm;
import com.example.testApp.model.Book;
import com.example.testApp.model.File;
import com.example.testApp.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.List;


@Controller
public class BookMvcController {

    public class ImageUtil {
        public String getImgData(File file){
            if (file == null) return "";
            var byteData = file.getContent();
            if (byteData == null || byteData.length == 0) return "";
            return "data:image/jpeg;base64, " + Base64.getEncoder().encodeToString(byteData);
        }
    }

   ImageUtil imageUtil = new ImageUtil();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final int ROW_PER_PAGE = 5;

    @Autowired
    private BookService bookService;

    @GetMapping(value = "/books")
    public String getBooks(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<Book> books = bookService.findAll(pageNumber, ROW_PER_PAGE);

        long count = bookService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("books", books);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);
        return "book/bookList1";
    }

    @GetMapping(value = "/books/{bookId}")
    public String getBookById(Model model, @PathVariable long bookId) {
        Book book = null;
        try {
            book = bookService.findById(bookId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Book not found");
        }
        model.addAttribute("book", book);
        return "book/book";
    }

    @GetMapping(value = {"/books/add"})
    public String showAddBook(Model model) {
        var book = new BookForm();
        model.addAttribute("add", true);
        model.addAttribute("book", book);

        return "book/bookEdit";
    }

    @PostMapping(value = "/books/add")
    public String addBook(Model model, @ModelAttribute("book") BookForm bookForm) {
        try {
            var book = new Book();
            book.setTitle(bookForm.getTitle());
            book.setDescription(bookForm.getDescription());
//            book.setAuthors(bookForm.getAuthors());
//            book.setGenres(bookForm.getGenres());
            book.setCover(bookForm.getCover());
            book.setContent(bookForm.getContent());

            Book newBook = bookService.save(book);
            return "redirect:/books";
        } catch (Exception ex) {

            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            model.addAttribute("add", true);
            return "book/bookEdit";
        }
    }

    @GetMapping(value = {"/books/{bookId}/edit"})
    public String showEditBook(Model model, @PathVariable long bookId) {
        BookForm bookForm = null;
        try {
           var book = bookService.findById(bookId);
           if (book != null)
               bookForm = new BookForm(book);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Book not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("book", bookForm);
        return "book/bookEdit";
    }

    @PostMapping(value = {"/books/{bookId}/edit"})
    public String updateBook(Model model, @PathVariable long bookId, @ModelAttribute("book") Book book) {
        try {
            book.setId(bookId);
            bookService.update(book);
            return "redirect:/books/" + String.valueOf(book.getId());
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            model.addAttribute("add", false);
            return "book/bookEdit";
        }
    }

    @GetMapping(value = {"/books/{bookId}/delete"})
    public String showDeleteBookById(Model model, @PathVariable long bookId) {
        Book book = null;
        try {
            book = bookService.findById(bookId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Book not found");
        }
        model.addAttribute("allowDelete", true);
        model.addAttribute("book", book);
        return "book/book";
    }

    @PostMapping(value = {"/books/{bookId}/delete"})
    public String deleteBookById(Model model, @PathVariable long bookId) {
        try {
            bookService.deleteById(bookId);
            return "redirect:/books";
        } catch (ResourceNotFoundException ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "book/book";
        }
    }

    @GetMapping("/downloadfile")
    public void downloadFile(@Param("id") Long id , Model model, HttpServletResponse response) throws IOException, ResourceNotFoundException {
        Book book = bookService.findById(id);
        if(book!=null) {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename = "+ book.getContent().getName();
            response.setHeader(headerKey, headerValue);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(book.getContent().getContent());
            outputStream.close();
        }
    }

    @GetMapping("/image")
    public void showImage(@Param("id") Long id, HttpServletResponse response)
            throws ServletException, IOException, ResourceNotFoundException {

        var book = bookService.findById(id);
        if (book != null && book.getCover() != null) {
            response.setContentType("image/jpeg, image/jpg, image/png, image/gif, image/pdf");
            response.getOutputStream().write(book.getCover().getContent());
            response.getOutputStream().close();
        }
    }


}
