package pl.demo.mapper;

import java.util.List;

import pl.demo.entity.Book;
import pl.demo.to.BookTo;

public interface BookMapper {

    BookTo map2To(Book book);

    Book map(BookTo bookTo);

    List<BookTo> map2To(List<Book> bookEntities);

    List<Book> map2Entity(List<BookTo> bookEntities);
}
