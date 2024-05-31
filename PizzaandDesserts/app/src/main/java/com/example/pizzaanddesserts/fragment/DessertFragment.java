package com.example.pizzaanddesserts.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;

import com.example.pizzaanddesserts.CartManager;
import com.example.pizzaanddesserts.R;
import com.example.pizzaanddesserts.adapter.DessertAdapter;
import com.example.pizzaanddesserts.api.ApiService;
import com.example.pizzaanddesserts.api.RetrofitClient;
import com.example.pizzaanddesserts.models.DessertModel;

import java.net.UnknownHostException;
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
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dessert, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressdessert);
        searchView = view.findViewById(R.id.search_dessert);
        context = getContext();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setVisibility(View.GONE); // Sembunyikan RecyclerView secara awal

        apiService = RetrofitClient.getClient().create(ApiService.class);

        dessertAdapter = new DessertAdapter(dessertModel, context, (dessertModel1, orderCount) -> {
            CartManager.getInstance().addOrUpdateDessert(dessertModel1, orderCount);
        });
        recyclerView.setAdapter(dessertAdapter);

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
        recyclerView.setVisibility(View.GONE); // Sembunyikan RecyclerView saat mengambil data
        progressBar.setVisibility(View.VISIBLE); // Tampilkan ProgressBar

        // Handler untuk menunda eksekusi selama 2 detik
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Call<List<DessertModel>> call = apiService.getDessert();
            call.enqueue(new Callback<List<DessertModel>>() {
                @Override
                public void onResponse(Call<List<DessertModel>> call, Response<List<DessertModel>> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.isSuccessful() && response.body() != null) {
                        dessertModel.clear(); // Clear old data
                        if (query == null || query.isEmpty()) {
                            dessertModel.addAll(response.body());
                        } else {
                            for (DessertModel dessert : response.body()) {
                                if (dessert.getName().toLowerCase().contains(query.toLowerCase())) {
                                    dessertModel.add(dessert);
                                }
                            }
                            if (dessertModel.isEmpty()) {
                                Toast.makeText(context, "Data kosong", Toast.LENGTH_SHORT).show();
                            }
                        }
                        dessertAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE); // Tampilkan RecyclerView setelah data berhasil diambil
                    } else {
                        Toast.makeText(context, "Failed to get data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<DessertModel>> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    if (t instanceof UnknownHostException) {
                        Toast.makeText(context, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to get data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }, 2000); // 2000 milliseconds delay
    }
}
