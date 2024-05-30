package com.example.pizzaanddesserts.api;

import com.example.pizzaanddesserts.models.DessertModel;
import com.example.pizzaanddesserts.models.PizzaModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiService {
    String RAPID_API_KEY = "23f27a016bmsh0e7c72db3452422p1cefc5jsnc237f3518b65";
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
