package pl.libraryManager.service.implementation;

import pl.libraryManager.exception.ValidationException;
import pl.libraryManager.model.Book;
import pl.libraryManager.repository.IBookRepository;
import pl.libraryManager.service.IBookService;

import java.util.List;
import java.util.stream.Collectors;

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
        String lowerQuery = query.toLowerCase();
        return bookRepository.findAll().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(lowerQuery) ||
                        b.getAuthor().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    @Override
    public void addBook(String title, String author) {
        validateBookData(title, author);
        bookRepository.save(new Book(null, title, author));
    }

    @Override
    public void updateBook(Long id, String newTitle, String newAuthor) {
        validateBookData(newTitle, newAuthor);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Nie znaleziono książki o ID: " + id));
        book.setTitle(newTitle);
        book.setAuthor(newAuthor);
        bookRepository.save(book);
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
