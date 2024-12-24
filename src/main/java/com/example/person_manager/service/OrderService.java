package com.example.person_manager.service;

import com.example.person_manager.model.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

    private final List<Order> orders = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public void saveOrder(Order order) {
        order.setId(counter.incrementAndGet());
        orders.add(order);
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public Order getOrderById(Long orderId) {
        return orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElse(null);
    }

    public void deleteOrder(Long orderId) {
        orders.removeIf(order -> order.getId().equals(orderId));
    }
}
