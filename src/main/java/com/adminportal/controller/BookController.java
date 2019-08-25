package com.adminportal.controller;

import com.adminportal.domain.Book;
import com.adminportal.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;


    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addBook(Model model) {
        /*
         * create a new instance
         */
        Book book = new Book();
        model.addAttribute("book", book);
        return "addBook";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addBookPost(@ModelAttribute("book") Book book, HttpServletRequest request) {

        /*
         * save a book into the "DB" and generated "book_id"
         */
        bookService.save(book);

        /*
         * A representation of an uploaded file received in a multipart request.
         */
        MultipartFile bookImage = book.getBookImage();

        /*
         * writing the "image"-file to a "local storage (this machine)" from the form of the "page"
         * also in production you can use a "cloud-storage" (e.g.)
         * */
        try {
            byte[] bytes = bookImage.getBytes();
            String name = book.getId() + ".png";
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(new File("src/main/resources/static/image/book/" + name)));
            stream.write(bytes);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:bookList";
    }

    @RequestMapping("/bookList")
    public String bookList(Model model) {

        List<Book> bookList = bookService.findAll();
        model.addAttribute("bookList", bookList);
        return "bookList";

    }

}
