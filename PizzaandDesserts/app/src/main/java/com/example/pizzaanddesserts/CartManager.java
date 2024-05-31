package com.example.pizzaanddesserts;

import com.example.pizzaanddesserts.models.DessertModel;
import com.example.pizzaanddesserts.models.PizzaModel;

import java.util.HashMap;
import java.util.Map;

public class CartManager {
    private static CartManager instance;
    private Map<String, OrderItem> cartItems = new HashMap<>();

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addOrUpdatePizza(PizzaModel pizza, int quantity) {
        if (quantity == 0) {
            cartItems.remove(pizza.getName());
        } else {
            cartItems.put(pizza.getName(), new OrderItem(pizza, quantity));
        }
    }

    public void addOrUpdateDessert(DessertModel dessert, int quantity) {
        if (quantity == 0) {
            cartItems.remove(dessert.getName());
        } else {
            cartItems.put(dessert.getName(), new OrderItem(dessert, quantity));
        }
    }

    public Map<String, OrderItem> getCartItems() {
        return cartItems;
    }

    public int getTotalPrice() {
        int total = 0;
        for (OrderItem item : cartItems.values()) {
            total += item.getTotalPrice();
        }
        return total;
    }

    // Tambahkan metode ini untuk menghapus semua item di keranjang
    public void clearCart() {
        cartItems.clear();
    }
}
