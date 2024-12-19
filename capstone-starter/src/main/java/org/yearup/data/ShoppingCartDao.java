package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;


public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here

    // add item
    ShoppingCart addItem(int userId, int productId, int quantity);

    // update quantity
    ShoppingCart updateQuantity(int userId, int productId, int quantity);

    // clear items in shopping cart
    ShoppingCart clearItems(int userId);

    void save(ShoppingCart shoppingCart, int userId);
}
