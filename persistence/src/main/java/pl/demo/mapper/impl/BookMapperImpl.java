package pl.demo.mapper.impl;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import pl.demo.entity.Book;
import pl.demo.mapper.BookMapper;
import pl.demo.to.BookTo;

import org.springframework.stereotype.Component;

@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public BookTo map2To(Book book) {
        if (book != null) {
            return new BookTo(
                    book.getId(),
                    book.getTitle(),
                    new HashSet<>(book.getAuthors()),
                    book.getDescription(),
                    book.getStatus(),
                    new HashSet<>(book.getCategories()));
        }
        return null;
    }

    @Override
    public Book map(BookTo bookTo) {
        if (bookTo != null) {
            return new Book(
                    bookTo.getId(),
                    bookTo.getTitle(),
                    new HashSet<>(bookTo.getAuthors()),
                    bookTo.getDescription(),
                    bookTo.getStatus(),
                    new HashSet<>(bookTo.getCategories()));
        }
        return null;
    }

    @Override
    public List<BookTo> map2To(List<Book> bookEntities) {
        return bookEntities.stream().map(this::map2To).collect(Collectors.toList());
    }

    @Override
    public List<Book> map2Entity(List<BookTo> bookEntities) {
        return bookEntities.stream().map(this::map).collect(Collectors.toList());
    }
}
