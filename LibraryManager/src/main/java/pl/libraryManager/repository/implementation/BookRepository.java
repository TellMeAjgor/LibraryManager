package pl.libraryManager.repository.implementation;

import pl.libraryManager.config.DatabaseConfig;
import pl.libraryManager.model.Book;
import pl.libraryManager.model.BookStatus;
import pl.libraryManager.repository.IBookRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepository implements IBookRepository {

    @Override
    public List<Book> findAll() {
        return getBooksByQuery("SELECT * FROM books", null);
    }

    @Override
    public Optional<Book> findById(Long id) {
        List<Book> books = getBooksByQuery("SELECT * FROM books WHERE id = ?", id);
        return books.isEmpty() ? Optional.empty() : Optional.of(books.get(0));
    }

    @Override
    public void save(Book book) {
        String sql = "INSERT INTO books (title, author, publish_year, isbn, status, category_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setBookParams(stmt, book);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void update(Book book) {
        String sql = "UPDATE books SET title=?, author=?, publish_year=?, isbn=?, status=?, category_id=? WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setBookParams(stmt, book);
            stmt.setLong(7, book.getId());
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<Book> search(String query) {
        String sql = "SELECT * FROM books WHERE LOWER(title) LIKE ? OR LOWER(author) LIKE ?";
        String param = "%" + query.toLowerCase() + "%";
        List<Book> results = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, param);
            stmt.setString(2, param);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) results.add(mapRowToBook(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return results;
    }

    private List<Book> getBooksByQuery(String sql, Long paramId) {
        List<Book> results = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (paramId != null) stmt.setLong(1, paramId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) results.add(mapRowToBook(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return results;
    }

    private void setBookParams(PreparedStatement stmt, Book book) throws SQLException {
        stmt.setString(1, book.getTitle());
        stmt.setString(2, book.getAuthor());
        if (book.getPublishYear() != null) stmt.setInt(3, book.getPublishYear()); else stmt.setNull(3, Types.INTEGER);
        stmt.setString(4, book.getIsbn());
        stmt.setString(5, book.getStatus() != null ? book.getStatus().name() : BookStatus.AVAILABLE.name());
        if (book.getCategoryId() != null) stmt.setLong(6, book.getCategoryId()); else stmt.setNull(6, Types.BIGINT);
    }

    private Book mapRowToBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getInt("publish_year") == 0 ? null : rs.getInt("publish_year"),
                rs.getString("isbn"),
                BookStatus.valueOf(rs.getString("status")),
                rs.getLong("category_id") == 0 ? null : rs.getLong("category_id")
        );
    }
}