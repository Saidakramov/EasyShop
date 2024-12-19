package org.yearup.models;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
//
//@Table(name = "shopping_cart")
//@Entity
public class ShoppingCart
{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//    @Column(name = "user_id")
//    private int userId;
//    @Column(name = "product_id")
//    private int productId;
//    private int quantity;

    //@Transient
    private Map<Integer, ShoppingCartItem> items = new HashMap<>();

    public Map<Integer, ShoppingCartItem> getItems()
    {
        return items;
    }

    public void setItems(Map<Integer, ShoppingCartItem> items)
    {
        this.items = items;
    }

    public boolean contains(int productId)
    {
        return items.containsKey(productId);
    }

    public void add(ShoppingCartItem item)
    {
        items.put(item.getProductId(), item);
    }

    public ShoppingCartItem get(int productId)
    {
        return items.get(productId);
    }

    public BigDecimal getTotal()
    {

        return items.values()
                                .stream()
                                .map(ShoppingCartItem::getLineTotal)
                                .reduce( BigDecimal.ZERO, (lineTotal, subTotal) -> subTotal.add(lineTotal));
    }


}
