package pl.libraryManager.service;

import pl.libraryManager.model.Book;
import java.util.List;

public interface IBookService {
    List<Book> getAllBooks();
    List<Book> search(String query);
    void addBook(String title, String author, Long categoryId);
    void updateBook(Long id, String newTitle, String newAuthor, Long newCategoryId);
    void deleteBook(Long id);
}