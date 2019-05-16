package pl.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.demo.entity.Book;
import pl.demo.exception.BusinessException;
import pl.demo.mapper.BookMapper;
import pl.demo.repository.BookRepository;
import pl.demo.service.BookService;
import pl.demo.to.BookTo;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

	private BookRepository bookRepository;

	private BookMapper bookMapper;

	@Autowired
	public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
		this.bookRepository = bookRepository;
		this.bookMapper = bookMapper;
	}

	@Override
	public List<BookTo> findAllBooks() {
		return bookMapper.map2To(bookRepository.findAll());
	}

	@Override
	public BookTo findById(Long id) {
		Optional<Book> book = bookRepository.findById(id);
		if (!book.isPresent()) {
			throw new BusinessException();
		}
		return bookMapper.map2To(book.get());
	}

	@Override
	public List<BookTo> findBooksByTitle(String title) {
		return bookMapper.map2To(bookRepository.findBookByTitle(title));
	}

	@Override
	@Transactional
	public BookTo saveBook(BookTo book) {
		Book entity = bookMapper.map(book);
		entity = bookRepository.save(entity);
		return bookMapper.map2To(entity);
	}

	@Override
	@Transactional
	public void deleteBook(Long id) {
		bookRepository.deleteById(id);

	}

	private boolean filterTitle(Book b, String titleSubstring) {
		return titleSubstring == null || b.getTitle().toLowerCase().contains(titleSubstring.toLowerCase());
	}

	private boolean filterAuthors(Book book, String authorSubstring) {
		return authorSubstring == null
				|| book.getAuthors().stream().anyMatch(a -> a.toLowerCase().contains(authorSubstring.toLowerCase()));
	}

	@Override
	public List<BookTo> filterBooks(String titleSubstring, String authorSubstring, String status) {
		List<Book> result = StreamSupport.stream(bookRepository.findAll().spliterator(), false)
				.filter(b -> filterTitle(b, titleSubstring)).filter(b -> filterAuthors(b, authorSubstring))
				.filter(b -> status == null || b.getStatus().toString().toLowerCase().contains(status.toLowerCase())).collect(Collectors.toList());

		return bookMapper.map2To(result);
	}
}
