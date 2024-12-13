package org.yearup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.models.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategoryId(int categoryId);
}
