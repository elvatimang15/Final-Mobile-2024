package com.example.pizzaanddesserts.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.pizzaanddesserts.CartManager;
import com.example.pizzaanddesserts.R;
import com.example.pizzaanddesserts.adapter.DessertAdapter;
import com.example.pizzaanddesserts.adapter.PizzaAdapter;
import com.example.pizzaanddesserts.api.ApiService;
import com.example.pizzaanddesserts.api.RetrofitClient;
import com.example.pizzaanddesserts.models.DessertModel;
import com.example.pizzaanddesserts.models.PizzaModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DessertFragment extends Fragment {
    private ApiService apiService;
    private DessertAdapter dessertAdapter;
    private RecyclerView recyclerView;
    private Context context;
    private ArrayList<DessertModel> dessertModel = new ArrayList<>();
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dessert, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressdessert);
        context = getContext();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        apiService = RetrofitClient.getClient().create(ApiService.class);

        dessertAdapter = new DessertAdapter(dessertModel, context, (dessertModel1, orderCount) -> {
            CartManager.getInstance().addOrUpdateDessert(dessertModel1, orderCount);
        });
        recyclerView.setAdapter(dessertAdapter);

        fetchDataFromApi();

        return view;
    }

    private void fetchDataFromApi() {
        progressBar.setVisibility(View.VISIBLE);
        Call<List<DessertModel>> call = apiService.getDessert();
        call.enqueue(new Callback<List<DessertModel>>() {
            @Override
            public void onResponse(Call<List<DessertModel>> call, Response<List<DessertModel>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    dessertModel.clear(); // Clear old data
                    dessertModel.addAll(response.body());
                    dessertAdapter.notifyDataSetChanged();
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<DessertModel>> call, Throwable t) {

            }
        });
    }
}