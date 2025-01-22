package com.example.person_manager.controller;

import com.example.person_manager.model.Order;
import com.example.person_manager.service.EmailService;
import com.example.person_manager.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final EmailService emailService;

    @Autowired
    public OrderController(OrderService orderService, EmailService emailService) {
        this.orderService = orderService;
        this.emailService = emailService;
    }

    // Просмотр всех заказов
    @GetMapping
    public String viewOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order/orders_list"; // Возвращаем вид для отображения списка заказов
    }

    // Одобрение заказа
    @PostMapping("/approve/{orderId}")
    public String approveOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            // Изменение статуса заказа на "Доставляется"
            order.setStatus("Доставляется");

            // Отправка письма клиенту с уведомлением
            String subject = "Заказ одобрен";
            String message = String.format("Ваш заказ #%d был одобрен и отправлен.", orderId);
            emailService.sendSimpleEmail(order.getEmail(), subject, message);

            // Здесь можно сохранить изменения в базе данных, если нужно
        }
        return "redirect:/orders"; // Перенаправление на список заказов
    }

    // Удаление заказа
    @PostMapping("/delete/{orderId}")
    public String deleteOrder(@PathVariable Long orderId) {
        // Удаление заказа и, возможно, возврат товара в инвентарь
        orderService.deleteOrder(orderId);
        return "redirect:/orders"; // Перенаправление на список заказов
    }
}
