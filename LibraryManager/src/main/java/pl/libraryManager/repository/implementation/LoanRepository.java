package pl.libraryManager.repository.implementation;

import pl.libraryManager.config.DatabaseConfig;
import pl.libraryManager.model.Loan;
import pl.libraryManager.repository.ILoanRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanRepository implements ILoanRepository {
    @Override
    public void save(Loan loan) {
        String sql = "INSERT INTO loans (book_id, user_id, loan_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, loan.getBookId());
            stmt.setLong(2, loan.getUserId());
            stmt.setTimestamp(3, loan.getLoanDate() != null ? loan.getLoanDate() : new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void update(Loan loan) {
        String sql = "UPDATE loans SET return_date = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, loan.getReturnDate());
            stmt.setLong(2, loan.getId());
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<Loan> findActiveByUserId(Long userId) {
        String sql = "SELECT * FROM loans WHERE user_id = ? AND return_date IS NULL";
        List<Loan> results = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) results.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return results;
    }

    @Override
    public Loan findActiveByBookId(Long bookId) {
        String sql = "SELECT * FROM loans WHERE book_id = ? AND return_date IS NULL";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    private Loan mapRow(ResultSet rs) throws SQLException {
        return new Loan(rs.getLong("id"), rs.getLong("book_id"), rs.getLong("user_id"),
                rs.getTimestamp("loan_date"), rs.getTimestamp("return_date"));
    }
}