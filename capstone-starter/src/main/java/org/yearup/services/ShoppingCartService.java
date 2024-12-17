package org.yearup.services;

import org.springframework.stereotype.Service;
import org.yearup.models.ShoppingCart;
import org.yearup.repositories.ShoppingCartRepository;

import java.util.List;

@Service
public class ShoppingCartService {
    private ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public List<ShoppingCart> getByUserId(int userId) {
        return shoppingCartRepository.findByUserId(userId);
    }

    public List<ShoppingCart> getAll() {
        return shoppingCartRepository.findAll();
    }

    public void addProduct( int userId, int productId, int quantity) {
        shoppingCartRepository.addShoppingCart(userId, productId, quantity);
    }

}
