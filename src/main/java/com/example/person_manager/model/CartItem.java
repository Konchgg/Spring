package com.example.person_manager.model;

import java.math.BigDecimal;

public class CartItem {
    private Knife knife;
    private int quantity;

    public CartItem(Knife knife, int quantity) {
        this.knife = knife;
        this.quantity = quantity;
    }

    // Getters и Setters

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

    public BigDecimal getTotalPrice() {
        return knife.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
