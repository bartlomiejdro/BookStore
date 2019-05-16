package pl.demo.repository;


import java.util.List;

import pl.demo.entity.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select book from Book book where upper(book.title) like concat(upper(:title), '%')")
    List<Book> findBookByTitle(@Param("title") String title);
    
  

}
