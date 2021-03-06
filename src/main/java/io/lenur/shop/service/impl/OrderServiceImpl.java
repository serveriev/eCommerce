package io.lenur.shop.service.impl;

import io.lenur.di.annotation.Inject;
import io.lenur.shop.dao.OrderDao;
import io.lenur.shop.domain.Cart;
import io.lenur.shop.domain.Order;
import io.lenur.shop.domain.User;
import io.lenur.shop.exception.ModelNotFoundException;
import io.lenur.shop.service.CartService;
import io.lenur.shop.service.OrderService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;

    @Inject
    private CartService cartService;

    @Override
    public Order completeOrder(Cart cart, User user) {
        Order order = new Order();

        order.setProducts(List.copyOf(cart.getProducts()));
        order.setUser(user);
        cartService.clear(cart);

        return orderDao.create(order);
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderDao.getUserOrders(userId);
    }

    @Override
    public Order get(Long id) {
        Optional<Order> order = orderDao.get(id);

        return order.orElse(null);
    }

    @Override
    public List<Order> getAll() {
        return Collections.unmodifiableList(orderDao.getAll());
    }

    @Override
    public Order create(Order order) {
        return orderDao.create(order);
    }

    @Override
    public Order update(Order order) {
        Objects.requireNonNull(order);

        Order orderPersist = get(order.getId());
        if (orderPersist == null) {
            throw new ModelNotFoundException();
        }

        orderPersist.setProducts(order.getProducts());
        orderPersist.setUser(order.getUser());

        return orderDao.update(orderPersist);
    }

    @Override
    public boolean delete(Long id) {
        return orderDao.delete(id);
    }
}