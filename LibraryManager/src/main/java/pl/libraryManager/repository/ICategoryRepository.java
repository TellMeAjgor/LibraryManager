package pl.libraryManager.repository;

import pl.libraryManager.model.Category;
import java.util.List;

public interface ICategoryRepository {
    List<Category> findAll();
    void save(String name);
    void delete(Long id);
}