package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao
{
    public MySqlShoppingCartDao(DataSource dataSource)
    {
        super(dataSource);
    }

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

    //You can add stubs or full implementations of these next
    @Override
    public void addProduct(int userId, int productId)
    {
        String selectSql = "SELECT quantity FROM shopping_cart WHERE user_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
        String updateSql = "UPDATE shopping_cart SET quantity = quantity + 1 WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection())
        {
            PreparedStatement selectStmt = connection.prepareStatement(selectSql);
            selectStmt.setInt(1, userId);
            selectStmt.setInt(2, productId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next())
            {
                // Product is already in cart; increment quantity
                PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                updateStmt.setInt(1, userId);
                updateStmt.setInt(2, productId);
                updateStmt.executeUpdate();
            }
            else
            {
                // Product not in cart; insert with quantity 1
                PreparedStatement insertStmt = connection.prepareStatement(insertSql);
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, productId);
                insertStmt.setInt(3, 1);
                insertStmt.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(int userId, int productId, int quantity)
    {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, productId);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void clearCart(int userId)
    {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

}
