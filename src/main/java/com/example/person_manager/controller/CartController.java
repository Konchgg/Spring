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

    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        model.addAttribute("cart", cart);
        return "order/cart";
    }

    @PostMapping("/add/{knifeId}")
    public String addItemToCart(@PathVariable Long knifeId, @RequestParam int quantity, HttpSession session) {
        Knife knife = knifeService.findById(knifeId);
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        if (knife != null) {
            int availableQuantity = knife.getQuantity();
            int currentCartQuantity = cart.getQuantityForKnife(knifeId);

            if (availableQuantity >= (quantity + currentCartQuantity)) {
                cart.addItem(knife, quantity);
                session.setAttribute("cart", cart);
            } else {
                String errorMessage = "Недостаточно товара в наличии. Доступно: " + (availableQuantity - currentCartQuantity);
                session.setAttribute("stockError", errorMessage);
            }
        } else {
            String errorMessage = "Нож с идентификатором " + knifeId + " не найден.";
            session.setAttribute("stockError", errorMessage);
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove/{knifeId}")
    public String removeItemFromCart(@PathVariable Long knifeId, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.removeItem(knifeId);
            session.setAttribute("cart", cart);
        }
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {
        model.addAttribute("order", new Order());
        return "order/checkout";
    }

    @PostMapping("/order/submit")
    public String submitOrder(@Valid @ModelAttribute Order order, BindingResult bindingResult, HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("formError", "Пожалуйста, исправьте ошибки в форме.");
            return "order/checkout";
        }

        boolean stockAvailable = checkStockAvailability(cart);

        if (stockAvailable) {
            updateStockLevels(cart);

            order.setItems(cart.getItems());
            orderService.saveOrder(order);

            session.removeAttribute("cart");
            return "redirect:/cart/order/success";
        } else {
            model.addAttribute("formError", "На складе недостаточно товара.");
            return "order/checkout";
        }
    }

    @GetMapping("/order/success")
    public String orderSuccess(Model model) {
        return "order/order_success";
    }

    private boolean checkStockAvailability(Cart cart) {
        for (CartItem item : cart.getItems().values()) {
            Knife knife = item.getKnife();
            if (item.getQuantity() > knife.getQuantity()) {
                return false;
            }
        }
        return true;
    }

    private void updateStockLevels(Cart cart) {
        for (CartItem item : cart.getItems().values()) {
            knifeService.updateKnifeQuantity(item.getKnife().getId(), -item.getQuantity());
        }
    }
}