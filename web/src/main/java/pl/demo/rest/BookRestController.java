package pl.demo.rest;

import java.util.List;

import pl.demo.service.BookService;
import pl.demo.to.BookTo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class BookRestController {

    private BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookTo>> findAllBooks(){
        List<BookTo> allBooks = bookService.findAllBooks();
        return ResponseEntity.ok().body(allBooks);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookTo> findBook(@PathVariable("id") Long id){
        if(id < 0){
            return ResponseEntity.badRequest().body(null);
        }
        BookTo book = bookService.findById(id);
        return ResponseEntity.ok().body(book);
    }

    @PostMapping("/books")
    public ResponseEntity<BookTo> addBook(@RequestBody BookTo bookTo){
        BookTo book = bookService.saveBook(bookTo);
        return ResponseEntity.ok().body(book);
    }

 }
