package org.yearup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.yearup.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
//    @Query(value = "INSERT INTO categories (name, description) VALUES(:name, :description) ", nativeQuery = true)
//    Category create(@Param("name") String name, @Param("description") String description);
    //void deleteById(int id);
}
