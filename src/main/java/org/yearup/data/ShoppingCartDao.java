package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    @Override
    public ShoppingCart getByUserId(int userId)
    {
        ShoppingCart cart = new ShoppingCart();

        String sql = "SELECT sc.product_id, sc.quantity, " +
                "       p.name, p.price, p.category_id, p.description, p.color, p.stock, p.image_url, p.featured " +
                "FROM shopping_cart sc " +
                "JOIN products p ON sc.product_id = p.product_id " +
                "WHERE sc.user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, userId);
            ResultSet row = statement.executeQuery();

            while (row.next())
            {
                Product product = new Product(
                        row.getInt("product_id"),
                        row.getString("name"),
                        row.getBigDecimal("price"),
                        row.getInt("category_id"),
                        row.getString("description"),
                        row.getString("color"),
                        row.getInt("stock"),
                        row.getBoolean("featured"),
                        row.getString("image_url")
                );

                int quantity = row.getInt("quantity");
                cart.addToCart(product, quantity);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return cart;
    }


    void addProduct(int userId, int productId);

    void updateQuantity(int userId, int productId, int quantity);

    void clearCart(int userId);
}
