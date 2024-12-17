package org.yearup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.yearup.models.ShoppingCart;

import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {
    List<ShoppingCart> findByUserId(int userId);
    @Query(value = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES(:userId, :productId, :quantity) ", nativeQuery = true)
    void addShoppingCart(@Param("userId") int userId, @Param("productId") int productId, @Param("quantity") int quantity);
}
