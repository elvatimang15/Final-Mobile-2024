package com.example.pizzaanddesserts.api;

import com.example.pizzaanddesserts.models.DessertModel;
import com.example.pizzaanddesserts.models.PizzaModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiService {
    String RAPID_API_KEY = "bce07b4bf7msh4eb8ba47cd1c3fbp132aefjsn6cc08338d36a";
    String RAPID_API_HOST = "pizza-and-desserts.p.rapidapi.com";

    @Headers({
            "X-RapidAPI-Key: " + RAPID_API_KEY,
            "X-RapidAPI-Host: " + RAPID_API_HOST
    })
    @GET("pizzas")
    Call<List<PizzaModel>> getPizza();

    @Headers({
            "X-RapidAPI-Key: " + RAPID_API_KEY,
            "X-RapidAPI-Host: " + RAPID_API_HOST
    })
    @GET("desserts")
    Call<List<DessertModel>> getDessert();
}
