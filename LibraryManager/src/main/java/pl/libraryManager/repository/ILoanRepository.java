package pl.libraryManager.repository;

import pl.libraryManager.model.Loan;
import java.util.List;

public interface ILoanRepository {
    void save(Loan loan);
    void update(Loan loan);
    List<Loan> findActiveByUserId(Long userId);
    Loan findActiveByBookId(Long bookId);
}