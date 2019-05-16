package pl.demo.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import pl.demo.enumerations.BookCategory;
import pl.demo.enumerations.BookStatus;

@Entity
@Table(name = "BOOK")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @ElementCollection
    @CollectionTable(name = "book_author", joinColumns = { @JoinColumn(name = "book_id") })
    @Column(name = "author", length = 40, nullable = false)
    private Set<String> authors;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @Column(nullable = false, length = 250)
    private String description;

    @ElementCollection
    @JoinTable(name = "book_category", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "interest", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<BookCategory> categories;

    public Book() {
    }

    public Book(
            Long id,
            String title,
            Set<String> authors,
            String description,
            BookStatus status,
            Set<BookCategory> categories) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.description = description;
        this.categories = categories;
        this.setStatus(status);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<String> authors) {
        this.authors = authors;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<BookCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<BookCategory> categories) {
        this.categories = categories;
    }
}
