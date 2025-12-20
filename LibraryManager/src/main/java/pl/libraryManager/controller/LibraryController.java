package pl.libraryManager.controller;

import pl.libraryManager.exception.LibraryException;
import pl.libraryManager.model.Role;
import pl.libraryManager.model.User;
import pl.libraryManager.service.IAuthService;
import pl.libraryManager.service.IBookService;

import java.util.Scanner;

public class LibraryController {
    private final IAuthService authService;
    private final IBookService bookService;
    private final Scanner scanner;

    public LibraryController(IAuthService authService, IBookService bookService) {
        this.authService = authService;
        this.bookService = bookService;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("=== SYSTEM BIBLIOTECZNY (SOLID v11/10) ===");
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
        System.out.println("0. Wyjdz z programu");
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
                System.out.println("!!! Błąd logowania: Zły login lub hasło.");
            }
        }
        return true;
    }

    private void handleUserMenu() {
        User user = authService.getCurrentUser();
        System.out.println("\n--- MENU GŁÓWNE (" + user.getUsername() + " | " + user.getRole() + ") ---");
        System.out.println("1. Lista książek");
        System.out.println("2. Szukaj książki");
        System.out.println("9. Wyloguj");

        if (user.getRole() == Role.ADMIN) {
            System.out.println("3. [ADMIN] Dodaj książkę");
            System.out.println("4. [ADMIN] Edytuj książkę");
            System.out.println("5. [ADMIN] Usuń książkę");
        }
        System.out.print("> ");

        String cmd = scanner.nextLine();

        switch (cmd) {
            case "1":
                System.out.println("\n--- LISTA KSIĄŻEK ---");
                bookService.getAllBooks().forEach(System.out::println);
                break;
            case "2":
                System.out.print("Szukaj (tytuł/autor): ");
                bookService.search(scanner.nextLine()).forEach(System.out::println);
                break;
            case "3":
                addBookAction(user);
                break;
            case "4":
                editBookAction(user);
                break;
            case "5":
                deleteBookAction(user);
                break;
            case "9":
                authService.logout();
                System.out.println(">>> Wylogowano.");
                break;
            default:
                System.out.println("!!! Nieznana komenda.");
                break;
        }
    }

    private void addBookAction(User user) {
        if (user.getRole() != Role.ADMIN) {
            System.out.println("!!! Brak uprawnień.");
            return;
        }
        try {
            System.out.print("Tytuł: ");
            String t = scanner.nextLine();
            System.out.print("Autor: ");
            String a = scanner.nextLine();

            bookService.addBook(t, a);
            System.out.println(">>> Sukces: Dodano książkę.");
        } catch (LibraryException e) {
            System.out.println("!!! BŁĄD WALIDACJI: " + e.getMessage());
        }
    }

    private void editBookAction(User user) {
        if (user.getRole() != Role.ADMIN) {
            System.out.println("!!! Brak uprawnień.");
            return;
        }
        try {
            System.out.print("Podaj ID książki do edycji: ");
            Long id = Long.parseLong(scanner.nextLine());

            System.out.print("Nowy tytuł: ");
            String t = scanner.nextLine();
            System.out.print("Nowy autor: ");
            String a = scanner.nextLine();

            bookService.updateBook(id, t, a);
            System.out.println(">>> Sukces: Zaktualizowano książkę.");
        } catch (NumberFormatException e) {
            System.out.println("!!! BŁĄD: ID musi być liczbą.");
        } catch (LibraryException e) {
            System.out.println("!!! BŁĄD: " + e.getMessage());
        }
    }

    private void deleteBookAction(User user) {
        if (user.getRole() != Role.ADMIN) {
            System.out.println("!!! Brak uprawnień.");
            return;
        }
        try {
            System.out.print("Podaj ID książki do usunięcia: ");
            Long id = Long.parseLong(scanner.nextLine());

            bookService.deleteBook(id);
            System.out.println(">>> Sukces: Usunięto książkę.");
        } catch (NumberFormatException e) {
            System.out.println("!!! BŁĄD: ID musi być liczbą.");
        } catch (LibraryException e) {
            System.out.println("!!! BŁĄD: " + e.getMessage());
        }
    }
}
