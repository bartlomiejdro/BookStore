package pl.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.demo.constants.ModelConstants;
import pl.demo.constants.ViewNames;
import pl.demo.service.BookService;
import pl.demo.to.BookTo;

@Controller
public class BookController {

	private static final String BOOK_HAS_BEEN_ADDED = "Book has been added";
	private static final String BOOK_HAS_BEEN_REMOVED = "book has been removed";
	private static final String NO_TITLE = "No title. Please enter title";
	private static final String NO_AUTHOR = "No author. Please enter author";
	private static final String NO_DESCRYPION = "No descryption. Please enter descryption";
	private static final String NO_STATUS = "No status. Please enter status";
	private static final String NO_CATEGORY = "No category. Please enter category";

	private BookService bookService;

	@Autowired
	public BookController(BookService bookService) {
		this.bookService = bookService;

	}

	@GetMapping(value = "/books")
	public String viev(Model model) {
		List<BookTo> books = bookService.findAllBooks();
		model.addAttribute(ModelConstants.LISTEDBOOKS, books);
		return ViewNames.BOOKS;
	}

	@GetMapping(value = "/books/book")
	public String vievBookDetails(Model model, @RequestParam("id") Long id) {
		BookTo book = bookService.findById(id);
		model.addAttribute(ModelConstants.BOOK, book);
		return ViewNames.BOOK;
	}

	@GetMapping("/books/filter")
	public String getFilteredBooks(@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "author", required = false) String authors,
			@RequestParam(value = "status", required = false) String status, Model model) {

		List<BookTo> books = bookService.filterBooks(title, authors, status);
		model.addAttribute(ModelConstants.LISTEDBOOKS, books);
		return ViewNames.BOOKS;
	}

	@PostMapping(value = "/books/remove")
	public String delete(Model model, @RequestParam("id") Long id) {
		bookService.deleteBook(id);
		List<BookTo> books = bookService.findAllBooks();
		model.addAttribute(ModelConstants.LISTEDBOOKS, books);
		model.addAttribute(ModelConstants.ERROR_MESSAGE, BOOK_HAS_BEEN_REMOVED);
		return ViewNames.BOOKS;
	}

	@GetMapping("/books/add")
	public String addBook(Model model) {
		model.addAttribute("newBook", new BookTo());
		return ViewNames.ADD;
	}

	@PostMapping("/books/add")
	public String addBook(Model model, @ModelAttribute("newBook") BookTo bookTo) {

		if (bookTo.getTitle().isEmpty()) {
			model.addAttribute(ModelConstants.ERROR_MESSAGE, NO_TITLE);
			return ViewNames.ADD;
		}
		if (bookTo.getAuthors() == null || bookTo.getAuthors().isEmpty() || bookTo.getAuthors().contains("")) {
			model.addAttribute(ModelConstants.ERROR_MESSAGE, NO_AUTHOR);
			return ViewNames.ADD;
		}
		if (bookTo.getDescription().isEmpty()) {
			model.addAttribute(ModelConstants.ERROR_MESSAGE, NO_DESCRYPION);
			return ViewNames.ADD;
		}
		if (bookTo.getStatus() == null) {
			model.addAttribute(ModelConstants.ERROR_MESSAGE, NO_STATUS);
			return ViewNames.ADD;
		}
		if (bookTo.getCategories() == null || bookTo.getCategories().size() == 0) {
			model.addAttribute(ModelConstants.ERROR_MESSAGE, NO_CATEGORY);
			return ViewNames.ADD;
		}
		bookService.saveBook(bookTo);
		List<BookTo> books = bookService.findAllBooks();
		model.addAttribute(ModelConstants.LISTEDBOOKS, books);
		model.addAttribute(ModelConstants.ERROR_MESSAGE, BOOK_HAS_BEEN_ADDED);
		return ViewNames.BOOKS;
	}

	@GetMapping("/403")
	public String accesDenid(Model model) {
		return ViewNames.ACCESS;
	}
}
