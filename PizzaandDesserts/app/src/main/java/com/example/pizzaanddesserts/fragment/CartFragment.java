package com.example.pizzaanddesserts.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaanddesserts.CartManager;
import com.example.pizzaanddesserts.OrderItem;
import com.example.pizzaanddesserts.R;
import com.example.pizzaanddesserts.adapter.CartAdapter;
import com.example.pizzaanddesserts.sqlite.DbConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private TextView totalHarga;
    private Button btnTotal;
    private DbConfig dbConfig;
    private int userId; // Replace with the actual user ID logic

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        totalHarga = view.findViewById(R.id.total_harga);
        btnTotal = view.findViewById(R.id.btn_total);
        dbConfig = new DbConfig(getContext());

        // Ambil ID user dari database
        userId = dbConfig.getLoggedInUserId();

        // Debug: Print view references to log
        if (recyclerView == null) {
            Log.e("CartFragment", "RecyclerView is null");
        }
        if (totalHarga == null) {
            Log.e("CartFragment", "TotalHarga TextView is null");
        }
        if (btnTotal == null) {
            Log.e("CartFragment", "BtnTotal Button is null");
        }

        // Verify that the views are not null
        if (recyclerView == null || totalHarga == null || btnTotal == null) {
            throw new RuntimeException("View not found in fragment_cart.xml");
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Map<String, OrderItem> cartItems = CartManager.getInstance().getCartItems();
        List<OrderItem> orderItemList = new ArrayList<>(cartItems.values());
        cartAdapter = new CartAdapter(orderItemList);
        recyclerView.setAdapter(cartAdapter);

        btnTotal.setOnClickListener(v -> {
            int total = CartManager.getInstance().getTotalPrice();
            totalHarga.setText(String.format("Rp. %d", total));

            // Save the total price to the database
            if (userId != -1) {
                dbConfig.updateIncome(userId, total);
                Toast.makeText(getContext(), "Total price saved to database", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to get user ID", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
