package org.yearup.models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCart
{
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
        BigDecimal total = items.values()
                .stream()
                .map(i -> i.getLineTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total;
    }

    public void addToCart(Product product, int quantity)
    {
        int productId = product.getProductId();

        if (items.containsKey(productId))
        {
            ShoppingCartItem item = items.get(productId);
            item.setQuantity(item.getQuantity() + quantity);
        }
        else
        {
            ShoppingCartItem item = new ShoppingCartItem(product, quantity);
            items.put(productId, item);
        }
    }
}
