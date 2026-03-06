package pl.libraryManager.service.implementation;

import pl.libraryManager.exception.ValidationException;
import pl.libraryManager.model.Category;
import pl.libraryManager.repository.ICategoryRepository;
import pl.libraryManager.service.ICategoryService;

import java.util.List;

public class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepo;

    public CategoryService(ICategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public void addCategory(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Nazwa kategorii nie może być pusta.");
        }
        categoryRepo.save(name);
    }

    @Override
    public void removeCategory(Long id) {
        categoryRepo.delete(id);
    }
}