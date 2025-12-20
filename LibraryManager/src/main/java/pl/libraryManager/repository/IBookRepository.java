package pl.libraryManager.repository;

import pl.libraryManager.model.Book;

import java.util.List;
import java.util.Optional;

public interface IBookRepository {
    List<Book> findAll();
    Optional<Book> findById(Long id);
    void save(Book book);
    void delete(Long id);
}
