package pl.libraryManager;

import pl.libraryManager.controller.LibraryController;
import pl.libraryManager.model.Role;
import pl.libraryManager.model.User;
import pl.libraryManager.repository.IUserRepository;
import pl.libraryManager.repository.implementation.BookRepository;
import pl.libraryManager.repository.implementation.CategoryRepository;
import pl.libraryManager.repository.implementation.LoanRepository;
import pl.libraryManager.repository.implementation.UserRepository;
import pl.libraryManager.service.IAuthService;
import pl.libraryManager.service.IBookService;
import pl.libraryManager.service.ICategoryService;
import pl.libraryManager.service.implementation.AuthService;
import pl.libraryManager.service.implementation.BookService;
import pl.libraryManager.service.implementation.CategoryService;
import pl.libraryManager.service.implementation.LoanService;
import pl.libraryManager.utils.SecurityUtils;

public class Main {
    public static void main(String[] args) {
        var userRepo = new UserRepository();
        var bookRepo = new BookRepository();
        var categoryRepo = new CategoryRepository();
        var loanRepo = new LoanRepository();

        IAuthService authService = new AuthService(userRepo);
        IBookService bookService = new BookService(bookRepo);
        ICategoryService categoryService = new CategoryService(categoryRepo);
        var loanService = new LoanService(loanRepo, bookRepo);

        var appController = new LibraryController(authService, bookService, loanService, categoryService);

        seedData(userRepo, bookService, categoryService);

        appController.run();
    }

    private static void seedData(IUserRepository userRepo, IBookService bookService, ICategoryService categoryService) {
        if (userRepo.findByUsername("admin").isEmpty()) {
            userRepo.save(new User(null, "admin", SecurityUtils.hashPassword("admin123"), Role.ADMIN));
        }
        if (userRepo.findByUsername("user").isEmpty()) {
            userRepo.save(new User(null, "user", SecurityUtils.hashPassword("user123"), Role.USER));
        }

        try {
            if (categoryService.getAllCategories().isEmpty()) {
                categoryService.addCategory("Fantasy");
                categoryService.addCategory("IT");
            }
            if (bookService.getAllBooks().isEmpty()) {
                bookService.addBook("Wiedźmin: Ostatnie Życzenie", "Andrzej Sapkowski", 1L);
                bookService.addBook("Pan Tadeusz", "Adam Mickiewicz", null);
                bookService.addBook("Czysty Kod", "Robert C. Martin", 2L);
            }
        } catch (Exception e) {
            System.out.println("Błąd seedowania danych: " + e.getMessage());
        }
    }
}