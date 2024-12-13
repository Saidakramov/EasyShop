package org.yearup.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Entity
public class ShoppingCart
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    private int productId;
    private int quantity;

    public ShoppingCart(int id, int userId, int productId, int quantity) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public ShoppingCart() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    //    private Map<Integer, ShoppingCartItem> items = new HashMap<>();
//
//    public Map<Integer, ShoppingCartItem> getItems()
//    {
//        return items;
//    }
//
//    public void setItems(Map<Integer, ShoppingCartItem> items)
//    {
//        this.items = items;
//    }
//
//    public boolean contains(int productId)
//    {
//        return items.containsKey(productId);
//    }
//
//    public void add(ShoppingCartItem item)
//    {
//        items.put(item.getProductId(), item);
//    }
//
//    public ShoppingCartItem get(int productId)
//    {
//        return items.get(productId);
//    }
//
//    public BigDecimal getTotal()
//    {
//        BigDecimal total = items.values()
//                                .stream()
//                                .map(i -> i.getLineTotal())
//                                .reduce( BigDecimal.ZERO, (lineTotal, subTotal) -> subTotal.add(lineTotal));
//
//        return total;
//    }

}
