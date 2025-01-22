package com.example.person_manager.service;

import com.example.person_manager.model.CartItem;
import com.example.person_manager.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

// Сервис для управления заказами
@Service
public class OrderService {

    // Хранилище заказов в памяти (для простоты, без базы данных)
    private final List<Order> orders = new ArrayList<>();
    // Счётчик для генерации уникальных ID заказов
    private final AtomicLong counter = new AtomicLong();
    // Зависимость сервиса ножей для управления количеством на складе
    private final KnifeService knifeService;

    // Конструктор с внедрением зависимостей
    @Autowired
    public OrderService(KnifeService knifeService) {
        this.knifeService = knifeService;
    }

    // Сохранение нового заказа
    public void saveOrder(Order order) {
        order.setId(counter.incrementAndGet()); // Уникальный ID для заказа
        orders.add(order); // Добавление заказа в список
    }

    // Получение всех заказов
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders); // Возвращение копии списка заказов
    }

    // Поиск заказа по ID
    public Order getOrderById(Long orderId) {
        return orders.stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElse(null); // Возвращение заказа или null, если не найден
    }

    // Удаление заказа и возврат товаров на склад
    public void deleteOrder(Long orderId) {
        Order order = getOrderById(orderId);
        if (order != null) {
            // Возврат каждого товара из заказа на склад
            for (CartItem item : order.getItems().values()) {
                knifeService.updateKnifeQuantity(item.getKnife().getId(), item.getQuantity());
                // Опционально: добавление ножа обратно в список (может быть избыточным)
                knifeService.addKnife(item.getKnife());
            }
            // Удаление заказа из списка
            orders.removeIf(o -> o.getId().equals(orderId));
        }
    }
}
