package pl.libraryManager.service;

import pl.libraryManager.model.Category;
import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();
    void addCategory(String name);
    void removeCategory(Long id);
}