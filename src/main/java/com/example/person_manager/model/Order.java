package com.example.person_manager.model;

import java.math.BigDecimal;
import java.util.Map;

public class Order {
    private Long id;
    private String fullName; // Полное имя заказчика
    private String email; // Электронная почта заказчика
    private String phone; // Телефон заказчика
    private String address; // Адрес доставки
    private Map<Long, CartItem> items; // Карта для хранения товаров по ID ножа
    private String status; // Статус заказа

    // Конструктор по умолчанию
    public Order() {
        // При создании заказа статус по умолчанию "Ожидание"
        this.status = "Ожидание";
    }

    // Геттеры и сеттеры для доступа к полям

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<Long, CartItem> getItems() {
        return items;
    }

    public void setItems(Map<Long, CartItem> items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Метод для расчета общей стоимости заказа
    public BigDecimal getTotalPrice() {
        return items.values().stream()
                .map(item -> item.getKnife().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
