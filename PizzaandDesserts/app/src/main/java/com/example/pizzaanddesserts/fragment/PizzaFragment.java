package com.example.pizzaanddesserts.fragment;

import static com.example.pizzaanddesserts.api.RetrofitClient.getClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.pizzaanddesserts.CartManager;
import com.example.pizzaanddesserts.R;
import com.example.pizzaanddesserts.adapter.PizzaAdapter;
import com.example.pizzaanddesserts.api.ApiService;
import com.example.pizzaanddesserts.api.RetrofitClient;
import com.example.pizzaanddesserts.models.PizzaModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PizzaFragment extends Fragment {

    private ApiService apiService;
    private PizzaAdapter pizzaAdapter;
    private RecyclerView recyclerView;
    private Context context;
    private ArrayList<PizzaModel> pizzaModel = new ArrayList<>();
    ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pizza, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progresspizza);
        context = getContext();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        apiService = RetrofitClient.getClient().create(ApiService.class);

        pizzaAdapter = new PizzaAdapter(pizzaModel, context, (pizzaModel1, orderCount) -> {
            CartManager.getInstance().addOrUpdatePizza(pizzaModel1, orderCount);
        });
        recyclerView.setAdapter(pizzaAdapter);

        fetchDataFromApi();

        return view;
    }

    private void fetchDataFromApi() {
        progressBar.setVisibility(View.VISIBLE);
        Call<List<PizzaModel>> call = apiService.getPizza();
        call.enqueue(new Callback<List<PizzaModel>>() {
            @Override
            public void onResponse(Call<List<PizzaModel>> call, Response<List<PizzaModel>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    pizzaModel.clear(); // Clear old data
                    pizzaModel.addAll(response.body());
                    pizzaAdapter.notifyDataSetChanged();
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<PizzaModel>> call, Throwable t) {
                Log.d("anu", t.getMessage().toString());
            }
        });
    }
}