package pl.libraryManager.model;

import java.sql.Timestamp;

public class Loan {
    private Long id;
    private Long bookId;
    private Long userId;
    private Timestamp loanDate;
    private Timestamp returnDate;

    public Loan() {}

    public Loan(Long id, Long bookId, Long userId, Timestamp loanDate, Timestamp returnDate) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Timestamp getLoanDate() { return loanDate; }
    public void setLoanDate(Timestamp loanDate) { this.loanDate = loanDate; }
    public Timestamp getReturnDate() { return returnDate; }
    public void setReturnDate(Timestamp returnDate) { this.returnDate = returnDate; }

    @Override
    public String toString() {
        return String.format("Wypożyczenie ID: %d | Książka ID: %d | Od: %s | Do: %s",
                id, bookId, loanDate, (returnDate != null ? returnDate : "Wypożyczona"));
    }
}