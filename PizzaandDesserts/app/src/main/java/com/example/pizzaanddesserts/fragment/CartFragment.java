package com.example.pizzaanddesserts.fragment;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private TextView totalHarga;
    private TextView tvTimestamp;
    private Button btnTotal;
    private DbConfig dbConfig;
    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        totalHarga = view.findViewById(R.id.total_harga);
        tvTimestamp = view.findViewById(R.id.tv_timestamp);
        btnTotal = view.findViewById(R.id.btn_total);
        dbConfig = new DbConfig(getContext());

        userId = dbConfig.getLoggedInUserId();

        if (recyclerView == null) {
            Log.e("CartFragment", "RecyclerView is null");
        }
        if (totalHarga == null) {
            Log.e("CartFragment", "TotalHarga TextView is null");
        }
        if (btnTotal == null) {
            Log.e("CartFragment", "BtnTotal Button is null");
        }

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

            if (userId != -1) {
                dbConfig.updateIncome(userId, total);
                Toast.makeText(getContext(), "Total price saved to database", Toast.LENGTH_SHORT).show();

                long timestamp = System.currentTimeMillis();
                dbConfig.saveTimestamp(userId, timestamp);
                loadTimestamp(timestamp);

                // Hapus pesanan setelah total dihitung
                clearCart();
            } else {
                Toast.makeText(getContext(), "Failed to get user ID", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void loadTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestampStr = sdf.format(new Date(timestamp));
        String displayText = "Created at " + timestampStr;
        tvTimestamp.setText(displayText);
    }

    private void clearCart() {
        CartManager.getInstance().clearCart();
        cartAdapter.notifyDataSetChanged();
    }
}
