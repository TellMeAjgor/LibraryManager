package pl.libraryManager.service.implementation;

import pl.libraryManager.exception.ValidationException;
import pl.libraryManager.model.Book;
import pl.libraryManager.model.BookStatus;
import pl.libraryManager.model.Loan;
import pl.libraryManager.repository.IBookRepository;
import pl.libraryManager.repository.ILoanRepository;
import pl.libraryManager.service.ILoanService;

import java.sql.Timestamp;
import java.util.List;

public class LoanService implements ILoanService {
    private final ILoanRepository loanRepo;
    private final IBookRepository bookRepo;

    public LoanService(ILoanRepository loanRepo, IBookRepository bookRepo) {
        this.loanRepo = loanRepo;
        this.bookRepo = bookRepo;
    }

    @Override
    public void borrowBook(Long bookId, Long userId) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ValidationException("Książka o podanym ID nie istnieje."));

        if (book.getStatus() == BookStatus.BORROWED) {
            throw new ValidationException("Książka jest już wypożyczona.");
        }

        book.setStatus(BookStatus.BORROWED);
        bookRepo.update(book);

        Loan loan = new Loan(null, bookId, userId, new Timestamp(System.currentTimeMillis()), null);
        loanRepo.save(loan);
    }

    @Override
    public void returnBook(Long bookId, Long userId) {
        Loan loan = loanRepo.findActiveByBookId(bookId);
        if (loan == null || !loan.getUserId().equals(userId)) {
            throw new ValidationException("Nie wypożyczyłeś tej książki.");
        }

        loan.setReturnDate(new Timestamp(System.currentTimeMillis()));
        loanRepo.update(loan);

        Book book = bookRepo.findById(bookId).orElseThrow();
        book.setStatus(BookStatus.AVAILABLE);
        bookRepo.update(book);
    }

    @Override
    public List<Loan> getUserLoans(Long userId) {
        return loanRepo.findActiveByUserId(userId);
    }
}