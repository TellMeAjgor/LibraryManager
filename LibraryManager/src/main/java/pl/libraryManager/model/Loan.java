package pl.libraryManager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Loan {
    private Long id;
    private Long bookId;
    private Long userId;
    private Timestamp loanDate;
    private Timestamp returnDate;
}