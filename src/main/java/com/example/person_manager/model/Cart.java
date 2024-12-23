package com.example.person_manager.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Cart {

    private Map<Long, CartItem> items = new HashMap<>();

    public void addItem(Knife knife, int quantity) {
        CartItem existingItem = items.get(knife.getId());
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            items.put(knife.getId(), new CartItem(knife, quantity));
        }
    }

    public CartItem removeItem(Long knifeId) {
        return items.remove(knifeId);
    }

    public int getQuantityForKnife(Long knifeId) {
        CartItem item = items.get(knifeId);
        return (item != null) ? item.getQuantity() : 0;
    }

    public Map<Long, CartItem> getItems() {
        return items;
    }

    // Новый метод для расчета общей стоимости
    public BigDecimal getTotal() {
        return items.values().stream()
                .map(item -> item.getKnife().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
