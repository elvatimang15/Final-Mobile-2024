package com.example.pizzaanddesserts.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaanddesserts.OrderItem;
import com.example.pizzaanddesserts.R;
import com.example.pizzaanddesserts.models.DessertModel;
import com.example.pizzaanddesserts.models.PizzaModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<OrderItem> orderItemList;

    public CartAdapter(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem orderItem = orderItemList.get(position);
        Object item = orderItem.getItem();
        int quantity = orderItem.getQuantity();
        double gtp = orderItem.getTotalPrice();
        if (item instanceof PizzaModel) {
            PizzaModel pizza = (PizzaModel) item;
            Picasso.get().load(pizza.getImg()).into(holder.imageView);
            holder.name.setText(pizza.getName());
            holder.quantity.setText(String.format("%d x", quantity));
        } else if (item instanceof DessertModel) {
            DessertModel dessert = (DessertModel) item;
            Picasso.get().load(dessert.getImg()).into(holder.imageView);
            holder.name.setText(dessert.getName());
            holder.quantity.setText(String.format("%d x", quantity));
        }
        Log.d("anu", String.valueOf(gtp));
        holder.totalPrice.setText(String.valueOf(gtp));
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, quantity, totalPrice;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.namecart);
            quantity = itemView.findViewById(R.id.cart);
            totalPrice = itemView.findViewById(R.id.total_harga);
        }
    }
}


