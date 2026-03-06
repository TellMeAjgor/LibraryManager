package pl.libraryManager.service;

import pl.libraryManager.model.Loan;
import java.util.List;

public interface ILoanService {
    void borrowBook(Long bookId, Long userId);
    void returnBook(Long bookId, Long userId);
    List<Loan> getUserLoans(Long userId);
}