package com.example.person_manager.model;

import java.math.BigDecimal;

public class CartItem {
    // Поля класса CartItem
    private Knife knife;  // Объект ножа, связанный с элементом корзины
    private int quantity; // Количество данного ножа в корзине

    // Конструктор класса CartItem
    public CartItem(Knife knife, int quantity) {
        this.knife = knife;
        this.quantity = quantity;
    }

    // Геттеры и сеттеры для доступа к полям

    public Knife getKnife() {
        return knife;
    }

    public void setKnife(Knife knife) {
        this.knife = knife;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Метод для получения общей стоимости данного элемента корзины
    public BigDecimal getTotalPrice() {
        return knife.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
