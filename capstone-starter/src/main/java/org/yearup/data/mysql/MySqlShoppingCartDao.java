package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.services.ProductService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    private final ProductService productService;

    public MySqlShoppingCartDao(DataSource dataSource, ProductService productService) {
        super(dataSource);
        this.productService = productService;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        String query = "SELECT * FROM shopping_cart WHERE user_id = ?";
        ShoppingCart shoppingCart = new ShoppingCart();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setInt(1, userId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("product_id");
                    int quantity = rs.getInt("quantity");

                    Product product = productService.getById(productId);
                    ShoppingCartItem item = new ShoppingCartItem(product, quantity);
                    shoppingCart.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shoppingCart;
    }

    @Override
    public ShoppingCart addItem(int userId, int productId, int quantity) {
        String query = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES(?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);

            int rowsAffected = ps.executeUpdate();
            // if successful retrieve the updated cart
            if (rowsAffected > 0) {
                return getByUserId(userId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ShoppingCart updateQuantity(int userId, int productId, int quantity) {
        String query = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, productId);


            int rowsAffected = ps.executeUpdate();
            // if successful retrieve the updated cart
            if (rowsAffected > 0) {
                return getByUserId(userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public ShoppingCart clearItems(int userId) {
        String query = "DELETE FROM shopping_cart WHERE user_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            int rowsAffected = ps.executeUpdate();
            // if successful return an empty cart
            if (rowsAffected > 0) {
                return new ShoppingCart();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(ShoppingCart shoppingCart, int userId) {
        // Iterate over the cart items and either insert or update each one
        for (ShoppingCartItem item : shoppingCart.getItems().values()) {
            String query = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE quantity = ?";
            try (Connection connection = getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, item.getProductId());
                preparedStatement.setInt(3, item.getQuantity());
                preparedStatement.setInt(4, item.getQuantity());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
