package com.example.person_manager.controller;

import com.example.person_manager.model.Cart;
import com.example.person_manager.model.CartItem;
import com.example.person_manager.model.Knife;
import com.example.person_manager.model.Order;
import com.example.person_manager.service.KnifeService;
import com.example.person_manager.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final KnifeService knifeService;
    private final OrderService orderService;

    @Autowired
    public CartController(KnifeService knifeService, OrderService orderService) {
        this.knifeService = knifeService;
        this.orderService = orderService;
    }

    // Просмотр текущих товаров в корзине
    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            // Инициализация новой корзины, если она не существует
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        model.addAttribute("cart", cart);
        return "order/cart";  // Возвращаем вид корзины
    }

    // Добавление товара в корзину
    @PostMapping("/add/{knifeId}")
    public String addItemToCart(@PathVariable Long knifeId, @RequestParam int quantity, HttpSession session) {
        Knife knife = knifeService.findById(knifeId); // Поиск ножа по ID
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart(); // Создаем новую корзину, если она не существует
            session.setAttribute("cart", cart);
        }
        if (knife != null) {
            int availableQuantity = knife.getQuantity();
            int currentCartQuantity = cart.getQuantityForKnife(knifeId);

            // Проверка доступности нужного количества
            if (availableQuantity >= (quantity + currentCartQuantity)) {
                cart.addItem(knife, quantity); // Добавляем товар в корзину
                session.setAttribute("cart", cart);
            } else {
                // Сохраняем сообщение об ошибке в сессии, если товара недостаточно
                String errorMessage = "Недостаточно товара в наличии. Доступно: " + (availableQuantity - currentCartQuantity);
                session.setAttribute("stockError", errorMessage);
            }
        } else {
            // Сообщение об ошибке, если нож не найден
            String errorMessage = "Нож с идентификатором " + knifeId + " не найден.";
            session.setAttribute("stockError", errorMessage);
        }
        return "redirect:/cart"; // Перенаправляем обратно на страницу корзины
    }

    // Удаление товара из корзины
    @PostMapping("/remove/{knifeId}")
    public String removeItemFromCart(@PathVariable Long knifeId, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.removeItem(knifeId); // Удаляем товар из корзины
            session.setAttribute("cart", cart);
        }
        return "redirect:/cart"; // Перенаправление на страницу корзины
    }

    // Просмотр страницы оформления заказа
    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {
        model.addAttribute("order", new Order()); // Подготавливаем новый заказ
        return "order/checkout"; // Возвращаем вид оформления заказа
    }

    // Отправка заказа на обработку
    @PostMapping("/order/submit")
    public String submitOrder(@Valid @ModelAttribute Order order, BindingResult bindingResult, HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/cart"; // Перенаправление на корзину, если корзина пуста
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("formError", "Пожалуйста, исправьте ошибки в форме.");
            return "order/checkout"; // Возврат к оформлению заказа, если есть ошибки валидации
        }

        boolean stockAvailable = checkStockAvailability(cart);

        if (stockAvailable) {
            updateStockLevels(cart); // Обновление уровня запасов

            order.setItems(cart.getItems());
            orderService.saveOrder(order); // Сохранение заказа

            session.removeAttribute("cart"); // Очистка сессии корзины
            return "redirect:/cart/order/success";
        } else {
            model.addAttribute("formError", "На складе недостаточно товара.");
            return "order/checkout"; // Возврат к оформлению заказа при недостатке товара
        }
    }

    // Страница успеха после размещения заказа
    @GetMapping("/order/success")
    public String orderSuccess(Model model) {
        return "order/order_success";
    }

    // Проверка наличия товара на складе
    private boolean checkStockAvailability(Cart cart) {
        for (CartItem item : cart.getItems().values()) {
            Knife knife = item.getKnife();
            if (item.getQuantity() > knife.getQuantity()) {
                return false; // Возврат false в случае недоступности товара
            }
        }
        return true; // Возврат true, если все товары доступны
    }

    // Обновление уровня запасов на складе
    private void updateStockLevels(Cart cart) {
        for (CartItem item : cart.getItems().values()) {
            knifeService.updateKnifeQuantity(item.getKnife().getId(), -item.getQuantity());
        }
    }
}
