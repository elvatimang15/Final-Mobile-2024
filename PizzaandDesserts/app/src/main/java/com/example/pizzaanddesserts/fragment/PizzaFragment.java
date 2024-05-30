package com.example.pizzaanddesserts.fragment;

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
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;

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
    SearchView searchView;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pizza, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progresspizza);
        searchView = view.findViewById(R.id.search_pizza); // Pastikan menggunakan androidx.appcompat.widget.SearchView
        context = getContext();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        apiService = RetrofitClient.getClient().create(ApiService.class);

        pizzaAdapter = new PizzaAdapter(pizzaModel, context, (pizzaModel1, orderCount) -> {
            CartManager.getInstance().addOrUpdatePizza(pizzaModel1, orderCount);
        });
        recyclerView.setAdapter(pizzaAdapter);

        fetchDataFromApi(null); // Fetch all data initially

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchDataFromApi(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    fetchDataFromApi(null);
                }
                return true;
            }
        });

        return view;
    }

    private void fetchDataFromApi(String query) {
        progressBar.setVisibility(View.VISIBLE);
        Call<List<PizzaModel>> call = apiService.getPizza();
        call.enqueue(new Callback<List<PizzaModel>>() {
            @Override
            public void onResponse(Call<List<PizzaModel>> call, Response<List<PizzaModel>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    pizzaModel.clear(); // Clear old data
                    if (query == null || query.isEmpty()) {
                        pizzaModel.addAll(response.body());
                    } else {
                        for (PizzaModel pizza : response.body()) {
                            if (pizza.getName().toLowerCase().contains(query.toLowerCase())) {
                                pizzaModel.add(pizza);
                            }
                        }
                        if (pizzaModel.isEmpty()) {
                            Toast.makeText(context, "Data kosong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    pizzaAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Failed to get data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PizzaModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d("anu", t.getMessage());
                Toast.makeText(context, "Failed to get data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
