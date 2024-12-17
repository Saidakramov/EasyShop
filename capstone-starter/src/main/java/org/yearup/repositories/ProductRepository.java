package org.yearup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.models.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategoryId(int categoryId);
    List<Product> findByPriceGreaterThan(BigDecimal minPrice);
    List<Product> findByPriceLessThan(BigDecimal minPrice);
    List<Product> findByColor(String color);
}
