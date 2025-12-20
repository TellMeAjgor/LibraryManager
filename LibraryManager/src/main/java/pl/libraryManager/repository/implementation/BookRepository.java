package pl.libraryManager.repository.implementation;

import pl.libraryManager.model.Book;
import pl.libraryManager.repository.IBookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class BookRepository implements IBookRepository {
    private final List<Book> books = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(books); // Zwracamy kopię
    }

    @Override
    public Optional<Book> findById(Long id) {
        return books.stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    @Override
    public void save(Book book) {
        if (book.getId() == null) {
            book.setId(idGenerator.getAndIncrement());
            books.add(book);
        } else {
            delete(book.getId());
            books.add(book);
        }
    }

    @Override
    public void delete(Long id) {
        books.removeIf(b -> b.getId().equals(id));
    }
}
