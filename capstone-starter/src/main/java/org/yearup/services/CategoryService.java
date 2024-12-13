package org.yearup.services;

import org.springframework.stereotype.Service;
import org.yearup.models.Category;
import org.yearup.repositories.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getById(int categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        return optionalCategory.orElse(null);
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public void update(int categoryId, Category category) {
        Category categoryToBeUpdated = getById(categoryId);

        if (categoryToBeUpdated != null) {
            categoryToBeUpdated = new Category(category.getCategoryId(), category.getName(), category.getDescription());

            categoryRepository.save(categoryToBeUpdated);
        }

    }

    public void delete(int categoryId) {
        categoryRepository.deleteById(categoryId);

    }

}

