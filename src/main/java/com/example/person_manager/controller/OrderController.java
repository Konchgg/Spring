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

    @GetMapping
    public String viewOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order/orders_list";
    }

    @PostMapping("/approve/{orderId}")
    public String approveOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            String subject = "Заказ одобрен";
            String message = String.format("Ваш заказ #%d был одобрен и отправлен.", orderId);
            emailService.sendSimpleEmail(order.getEmail(), subject, message);
            orderService.deleteOrder(orderId);
        }
        return "redirect:/orders";
    }

    @PostMapping("/delete/{orderId}")
    public String deleteOrder(@PathVariable Long orderId) {
        // Удаление заказа и возврат ножей в инвентарь
        orderService.deleteOrder(orderId);
        return "redirect:/orders";
    }
}
