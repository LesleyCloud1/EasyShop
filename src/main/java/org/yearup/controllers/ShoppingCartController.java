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
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController
{
    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;
    private final ProductDao productDao;

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
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        return shoppingCartDao.getByUserId(user.getId());
    }

    @PostMapping("/products/{productId}")
    public ShoppingCart addToCart(@PathVariable int productId, Principal principal)
    {
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);

        shoppingCartDao.addProduct(user.getId(), productId);

        return shoppingCartDao.getByUserId(user.getId());
    }

    @PutMapping("/products/{productId}")
    public void updateCartItem(@PathVariable int productId, @RequestBody ShoppingCartItem item, Principal principal)
    {
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);

        shoppingCartDao.updateQuantity(user.getId(), productId, item.getQuantity());
    }

    @DeleteMapping("")
    public void clearCart(Principal principal)
    {
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);

        shoppingCartDao.clearCart(user.getId());
    }
}
