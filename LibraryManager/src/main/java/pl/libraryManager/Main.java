package pl.libraryManager;

import pl.libraryManager.controller.LibraryController;
import pl.libraryManager.model.Role;
import pl.libraryManager.model.User;
import pl.libraryManager.repository.IUserRepository;
import pl.libraryManager.repository.implementation.BookRepository;
import pl.libraryManager.repository.implementation.UserRepository;
import pl.libraryManager.service.IAuthService;
import pl.libraryManager.service.IBookService;
import pl.libraryManager.service.implementation.AuthService;
import pl.libraryManager.service.implementation.BookService;
import pl.libraryManager.utils.SecurityUtils;

public class Main {
    public static void main(String[] args) {
        var userRepo = new UserRepository();
        var bookRepo = new BookRepository();

        IAuthService authService = new AuthService(userRepo);
        IBookService bookService = new BookService(bookRepo);

        var appController = new LibraryController(authService, bookService);

        seedData(userRepo, bookService);

        appController.run();
    }

    private static void seedData(IUserRepository userRepo, IBookService bookService) {
        userRepo.save(new User("admin", SecurityUtils.hashPassword("admin123"), Role.ADMIN));
        userRepo.save(new User("user", SecurityUtils.hashPassword("user123"), Role.USER));

        try {
            bookService.addBook("Wiedźmin: Ostatnie Życzenie", "Andrzej Sapkowski");
            bookService.addBook("Pan Tadeusz", "Adam Mickiewicz");
            bookService.addBook("Czysty Kod", "Robert C. Martin");
        } catch (Exception e) {
            System.out.println("Błąd przy dodawaniu danych startowych: " + e.getMessage());
        }
    }
}
