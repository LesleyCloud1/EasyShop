package org.yearup.models;

import java.math.BigDecimal;

public class ShoppingCartItem
{
    private Product product;
    private int quantity;
    private int discountPercent;

    public ShoppingCartItem() {
    }

    // Add this constructor
    public ShoppingCartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.discountPercent = 0;
    }

    public int getProductId() {
        return product.getProductId();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getLineTotal() {
        BigDecimal total = product.getPrice().multiply(new BigDecimal(quantity));
        if (discountPercent > 0) {
            BigDecimal discount = total.multiply(new BigDecimal(discountPercent)).divide(new BigDecimal(100));
            total = total.subtract(discount);
        }
        return total;
    }
}
