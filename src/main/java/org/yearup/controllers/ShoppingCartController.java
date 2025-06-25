package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController
{
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao)
    {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @GetMapping("")
    public ShoppingCart getCart(Principal principal)
    {
        try
        {
            //Get the currently logged-in username
            String userName = principal.getName();

            //Find the database user by username
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            //Use the shoppingCartDao to get all items in the cart and return it
            return shoppingCartDao.getByUserId(userId);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping("/products/{productId}")
    public void addToCart(@PathVariable int productId, Principal principal)
    {
        try
        {
            // Get the currently logged-in username
            String userName = principal.getName();

            // Find the user and get their user ID
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // Add the product to the cart
            shoppingCartDao.addProduct(userId, productId);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to add product to cart.");
        }
    }
    @PutMapping("/products/{productId}")
    public void updateCartItem(@PathVariable int productId, @RequestBody ShoppingCartItem item, Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // Update the quantity of the specified item in the user's cart
            shoppingCartDao.update(userId, productId, item.getQuantity());
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update product in cart.");
        }
    }

}
