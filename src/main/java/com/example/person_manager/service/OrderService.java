package com.example.person_manager.service;

import com.example.person_manager.model.CartItem;
import com.example.person_manager.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

    private final List<Order> orders = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();
    private final KnifeService knifeService; // Добавляем зависимость для KnifeService

    @Autowired
    public OrderService(KnifeService knifeService) {
        this.knifeService = knifeService;
    }

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
        Order order = getOrderById(orderId);
        if (order != null) {
            // Возвращаем товары на склад
            for (CartItem item : order.getItems().values()) {
                knifeService.updateKnifeQuantity(item.getKnife().getId(), item.getQuantity());
                // Добавляем нож обратно в список
                knifeService.addKnife(item.getKnife());
            }
            orders.removeIf(o -> o.getId().equals(orderId));
        }
    }
}

