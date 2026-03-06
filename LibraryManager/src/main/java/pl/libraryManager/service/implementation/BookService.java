package pl.libraryManager.service.implementation;

import pl.libraryManager.exception.ValidationException;
import pl.libraryManager.model.Book;
import pl.libraryManager.model.BookStatus;
import pl.libraryManager.repository.IBookRepository;
import pl.libraryManager.service.IBookService;

import java.util.List;

public class BookService implements IBookService {
    private final IBookRepository bookRepository;

    public BookService(IBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> search(String query) {
        if (query == null || query.trim().isEmpty()) return List.of();
        return bookRepository.search(query);
    }

    @Override
    public void addBook(String title, String author, Long categoryId) {
        validateBookData(title, author);
        bookRepository.save(new Book(null, title, author, null, null, BookStatus.AVAILABLE, categoryId));
    }

    @Override
    public void updateBook(Long id, String newTitle, String newAuthor, Long newCategoryId) {
        validateBookData(newTitle, newAuthor);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Nie znaleziono książki o ID: " + id));
        book.setTitle(newTitle);
        book.setAuthor(newAuthor);
        book.setCategoryId(newCategoryId);
        bookRepository.update(book);
    }

    @Override
    public void deleteBook(Long id) {
        if (bookRepository.findById(id).isEmpty()) {
            throw new ValidationException("Nie można usunąć. Książka o ID " + id + " nie istnieje.");
        }
        bookRepository.delete(id);
    }

    private void validateBookData(String title, String author) {
        if (title == null || title.trim().isEmpty()) throw new ValidationException("Tytuł wymagany!");
        if (author == null || author.trim().isEmpty()) throw new ValidationException("Autor wymagany!");
    }
}