package pl.demo.service;

import java.util.List;

import pl.demo.to.BookTo;

public interface BookService {

    List<BookTo> findAllBooks();
    BookTo findById(Long id);
    List<BookTo> findBooksByTitle(String title);
 
    BookTo saveBook(BookTo book);
    void deleteBook(Long id);
	List<BookTo> filterBooks(String titleSubstring, String authorSubstring, String status);
}
