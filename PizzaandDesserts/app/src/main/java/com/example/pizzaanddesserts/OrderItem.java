package com.example.pizzaanddesserts;

import android.util.Log;

import com.example.pizzaanddesserts.models.DessertModel;
import com.example.pizzaanddesserts.models.PizzaModel;

public class OrderItem {
    private Object item;
    private int quantity;

    public OrderItem(Object item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        int price = 0;
        if (item instanceof PizzaModel) {
            price = Integer.parseInt(((PizzaModel) item).getPrice()) * 10000;
        } else if (item instanceof DessertModel) {
            price = Integer.parseInt(((DessertModel) item).getPrice()) * 5000;
        }
        return price * quantity;
    }

    public Object getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }
}

