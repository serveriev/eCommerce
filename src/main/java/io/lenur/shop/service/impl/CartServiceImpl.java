package io.lenur.shop.service.impl;

import io.lenur.di.annotation.Inject;
import io.lenur.shop.dao.CartDao;
import io.lenur.shop.dao.impl.UserDao;
import io.lenur.shop.domain.Cart;
import io.lenur.shop.domain.Product;
import io.lenur.shop.domain.User;
import io.lenur.shop.service.CartService;
import io.lenur.shop.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartServiceImpl implements CartService {
    @Inject
    private CartDao cartDao;

    @Inject
    private UserService<User> userService;

    @Override
    public Cart create(Cart cart) {
        return cartDao.create(cart);
    }

    @Override
    public Cart addProduct(Cart cart, Product product) {
        List<Product> products = cart.getProducts();

        products.add(product);

        return cartDao.update(cart);
    }

    @Override
    public boolean deleteProduct(Cart cart, Product product) {
        List<Product> products = cart.getProducts();

        boolean result = products.removeIf(x -> x.getId().equals(product.getId()));
        if (result) {
            cartDao.update(cart);
        }

        return result;
    }

    @Override
    public void clear(Cart cart) {
        cart.setProducts(new ArrayList<>());
        cartDao.update(cart);
    }

    @Override
    public Cart getByUserId(final Long userId) {
        Optional<Cart> cart = cartDao.getByUserId(userId);

        if (cart.isPresent()) {
            return cart.get();
        }

        User user = userService.get(userId);
        Cart newCart = new Cart(new ArrayList<>(), user);

        return create(newCart);
    }

    @Override
    public boolean delete(Cart cart) {
        return cartDao.delete(cart.getId());
    }
}