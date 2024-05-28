package com.example.pizzaanddesserts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaanddesserts.R;
import com.example.pizzaanddesserts.models.PizzaModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.ViewHolder>{
    private List<PizzaModel> pizzaModel;
    private Context context;

    // Tambahkan interface untuk menangani klik
    public interface OnPizzaOrderListener {
        void onPizzaOrder(PizzaModel pizzaModel, int orderCount);
    }

    // Tambahkan listener di adapter
    private OnPizzaOrderListener pizzaOrderListener;

    public PizzaAdapter(List<PizzaModel> pizzaModel, Context context, OnPizzaOrderListener listener) {
        this.pizzaModel = pizzaModel;
        this.context = context;
        this.pizzaOrderListener = listener;
    }

    @NonNull
    @Override
    public PizzaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pizza, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PizzaAdapter.ViewHolder holder, int position) {
        holder.bind(pizzaModel.get(position));
    }

    @Override
    public int getItemCount() {
        return pizzaModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_img;
        TextView tv_name;
        TextView tv_veg;
        TextView tv_price;
        TextView tv_description;
        TextView tv_quantity;
        Button btn_kurang;
        Button btn_tambah;
        TextView order_quantity;
        int orderCount = 0;

        public ViewHolder(View view) {
            super(view);
            iv_img = view.findViewById(R.id.img);
            tv_name = view.findViewById(R.id.name);
            tv_veg = view.findViewById(R.id.veg);
            tv_price = view.findViewById(R.id.price);
            tv_description = view.findViewById(R.id.desc);
            tv_quantity = view.findViewById(R.id.quan);
            btn_kurang = view.findViewById(R.id.btn_kurang);
            btn_tambah = view.findViewById(R.id.btn_tambah);
            order_quantity = view.findViewById(R.id.order_quantity);
        }

        public void bind(PizzaModel pizzaModel) {
            // Load image using Picasso library
            Picasso.get().load(pizzaModel.getImg()).into(iv_img);
            tv_name.setText(pizzaModel.getName());
            tv_veg.setText(pizzaModel.getVeg());
            int originalPrice = Integer.parseInt(pizzaModel.getPrice());
            int newPrice = originalPrice * 10000;
            tv_price.setText(String.valueOf(newPrice));
            tv_description.setText(pizzaModel.getDescription());
            tv_quantity.setText(pizzaModel.getQuantity());
            order_quantity.setText(String.valueOf(orderCount));

            btn_tambah.setOnClickListener(v -> {
                int maxQuantity = Integer.parseInt(pizzaModel.getQuantity());
                if (orderCount < maxQuantity) {
                    orderCount++;
                    order_quantity.setText(String.valueOf(orderCount));
                    pizzaOrderListener.onPizzaOrder(pizzaModel, orderCount);
                }
            });

            btn_kurang.setOnClickListener(v -> {
                if (orderCount > 0) {
                    orderCount--;
                    order_quantity.setText(String.valueOf(orderCount));
                    pizzaOrderListener.onPizzaOrder(pizzaModel, orderCount);
                }
            });
        }
    }
}
