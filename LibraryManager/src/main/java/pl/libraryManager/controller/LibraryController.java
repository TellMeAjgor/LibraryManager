package pl.libraryManager.controller;

import pl.libraryManager.model.Role;
import pl.libraryManager.model.User;
import pl.libraryManager.service.IAuthService;
import pl.libraryManager.service.IBookService;
import pl.libraryManager.service.ICategoryService;
import pl.libraryManager.service.ILoanService;

import java.util.Scanner;

public class LibraryController {
    private final IAuthService authService;
    private final IBookService bookService;
    private final ILoanService loanService;
    private final ICategoryService categoryService;
    private final Scanner scanner;

    public LibraryController(IAuthService authService, IBookService bookService, ILoanService loanService, ICategoryService categoryService) {
        this.authService = authService;
        this.bookService = bookService;
        this.loanService = loanService;
        this.categoryService = categoryService;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("=== SYSTEM BIBLIOTECZNY (JDBC) ===");
        boolean running = true;

        while (running) {
            if (authService.getCurrentUser() == null) {
                running = handleLoginMenu();
            } else {
                handleUserMenu();
            }
        }
    }

    private boolean handleLoginMenu() {
        System.out.println("\n--- MENU LOGOWANIA ---");
        System.out.println("1. Zaloguj");
        System.out.println("0. Wyjdz");
        System.out.print("> ");

        String choice = scanner.nextLine();
        if ("0".equals(choice)) return false;

        if ("1".equals(choice)) {
            System.out.print("Login: ");
            String login = scanner.nextLine();
            System.out.print("Haslo: ");
            String pass = scanner.nextLine();

            if (authService.login(login, pass)) {
                System.out.println(">>> Zalogowano pomyślnie jako: " + authService.getCurrentUser().getRole());
            } else {
                System.out.println("!!! Błąd logowania.");
            }
        }
        return true;
    }

    private void handleUserMenu() {
        User user = authService.getCurrentUser();
        System.out.println("\n--- MENU GŁÓWNE (" + user.getUsername() + " | " + user.getRole() + ") ---");
        System.out.println("1. Lista książek");
        System.out.println("2. Szukaj książki");
        System.out.println("3. Wypożycz książkę");
        System.out.println("4. Oddaj książkę");
        System.out.println("5. Moje wypożyczenia");
        System.out.println("6. Kategorie");

        if (user.getRole() == Role.ADMIN) {
            System.out.println("7. [ADMIN] Dodaj książkę");
            System.out.println("8. [ADMIN] Edytuj książkę");
            System.out.println("9. [ADMIN] Usuń książkę");
            System.out.println("10. [ADMIN] Dodaj kategorię");
        }
        System.out.println("0. Wyloguj");
        System.out.print("> ");

        MenuCommand cmd = MenuCommand.fromCode(scanner.nextLine());

        try {
            switch (cmd) {
                case LIST_BOOKS: bookService.getAllBooks().forEach(System.out::println); break;
                case SEARCH_BOOKS:
                    System.out.print("Szukaj: ");
                    bookService.search(scanner.nextLine()).forEach(System.out::println);
                    break;
                case BORROW_BOOK: borrowAction(user); break;
                case RETURN_BOOK: returnAction(user); break;
                case MY_LOANS: loanService.getUserLoans(user.getId()).forEach(System.out::println); break;
                case CATEGORIES: categoryService.getAllCategories().forEach(System.out::println); break;
                case ADD_BOOK: addBookAction(user); break;
                case EDIT_BOOK: editBookAction(user); break;
                case DELETE_BOOK: deleteBookAction(user); break;
                case ADD_CATEGORY: addCategoryAction(user); break;
                case LOGOUT:
                    authService.logout();
                    System.out.println(">>> Wylogowano.");
                    break;
                default: System.out.println("!!! Nieznana komenda."); break;
            }
        } catch (NumberFormatException e) {
            System.out.println("!!! BŁĄD: Musisz wpisać poprawną liczbę.");
        } catch (Exception e) {
            System.out.println("!!! BŁĄD: " + e.getMessage());
        }
    }

    private void borrowAction(User user) {
        System.out.print("ID książki do wypożyczenia: ");
        Long id = Long.parseLong(scanner.nextLine());
        loanService.borrowBook(id, user.getId());
        System.out.println(">>> Sukces: Wypożyczono.");
    }

    private void returnAction(User user) {
        System.out.print("ID książki do oddania: ");
        Long id = Long.parseLong(scanner.nextLine());
        loanService.returnBook(id, user.getId());
        System.out.println(">>> Sukces: Oddano.");
    }

    private void addCategoryAction(User user) {
        if (user.getRole() != Role.ADMIN) { System.out.println("!!! Brak uprawnień."); return; }
        System.out.print("Nazwa kategorii: ");
        categoryService.addCategory(scanner.nextLine());
        System.out.println(">>> Sukces: Dodano.");
    }

    private void addBookAction(User user) {
        if (user.getRole() != Role.ADMIN) { System.out.println("!!! Brak uprawnień."); return; }
        System.out.print("Tytuł: "); String t = scanner.nextLine();
        System.out.print("Autor: "); String a = scanner.nextLine();
        System.out.print("ID Kategorii (lub enter by pominąć): "); String cStr = scanner.nextLine();
        Long c = cStr.isEmpty() ? null : Long.parseLong(cStr);

        bookService.addBook(t, a, c);
        System.out.println(">>> Sukces: Dodano książkę.");
    }

    private void editBookAction(User user) {
        if (user.getRole() != Role.ADMIN) { System.out.println("!!! Brak uprawnień."); return; }
        System.out.print("Podaj ID książki do edycji: "); Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Nowy tytuł: "); String t = scanner.nextLine();
        System.out.print("Nowy autor: "); String a = scanner.nextLine();
        System.out.print("Nowe ID Kategorii (lub enter): "); String cStr = scanner.nextLine();
        Long c = cStr.isEmpty() ? null : Long.parseLong(cStr);

        bookService.updateBook(id, t, a, c);
        System.out.println(">>> Sukces: Zaktualizowano.");
    }

    private void deleteBookAction(User user) {
        if (user.getRole() != Role.ADMIN) { System.out.println("!!! Brak uprawnień."); return; }
        System.out.print("Podaj ID książki do usunięcia: "); Long id = Long.parseLong(scanner.nextLine());
        bookService.deleteBook(id);
        System.out.println(">>> Sukces: Usunięto.");
    }
}