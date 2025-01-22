package com.example.person_manager.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Cart {

    // Хранение элементов корзины, используя карту, где ключем является ID ножа
    private Map<Long, CartItem> items = new HashMap<>();

    // Добавление товара в корзину
    public void addItem(Knife knife, int quantity) {
        CartItem existingItem = items.get(knife.getId());
        if (existingItem != null) {
            // Если товар уже в корзине, увеличиваем количество
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            // Если товара нет в корзине, добавляем его
            items.put(knife.getId(), new CartItem(knife, quantity));
        }
    }

    // Удаление товара из корзины по ID ножа
    public CartItem removeItem(Long knifeId) {
        return items.remove(knifeId);
    }

    // Получение количества товара по ID ножа
    public int getQuantityForKnife(Long knifeId) {
        CartItem item = items.get(knifeId);
        return (item != null) ? item.getQuantity() : 0;
    }

    // Получение всех элементов корзины
    public Map<Long, CartItem> getItems() {
        return items;
    }

    // Расчет общей стоимости товаров в корзине
    public BigDecimal getTotal() {
        return items.values().stream()
                .map(item -> item.getKnife().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
