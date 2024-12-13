package org.yearup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.models.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> getAllCategories();
    Category getById(int categoryId);
    Category create(Category category);
    void update(int categoryId, Category category);
    void delete(int categoryId);
}
