package pl.libraryManager.repository.implementation;

import pl.libraryManager.config.DatabaseConfig;
import pl.libraryManager.model.Category;
import pl.libraryManager.repository.ICategoryRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository implements ICategoryRepository {
    @Override
    public List<Category> findAll() {
        String sql = "SELECT * FROM categories";
        List<Category> results = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) results.add(new Category(rs.getLong("id"), rs.getString("name")));
        } catch (SQLException e) { e.printStackTrace(); }
        return results;
    }

    @Override
    public void save(String name) {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}