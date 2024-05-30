package com.example.pizzaanddesserts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaanddesserts.R;
import com.example.pizzaanddesserts.models.DessertModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DessertAdapter extends RecyclerView.Adapter<DessertAdapter.ViewHolder>{
    private List<DessertModel> dessertModel;
    private Context context;

    public interface OnDessertOrderListener {
        void onDessertOrder(DessertModel dessertModel, int orderCount);
    }

    // Tambahkan listener di adapter
    private OnDessertOrderListener dessertOrderListener;

    public DessertAdapter(List<DessertModel> dessertModel, Context context, OnDessertOrderListener listener) {
        this.dessertModel = dessertModel;
        this.context = context;
        this.dessertOrderListener = listener;
    }

    @NonNull
    @Override
    public DessertAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dessert, parent, false);
        return new DessertAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DessertAdapter.ViewHolder holder, int position) {
        holder.bind(dessertModel.get(position));
    }

    @Override
    public int getItemCount() {
        return dessertModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_imgd;
        TextView tv_named;
        TextView tv_priced;
        TextView tv_descriptiond;
        TextView tv_quantityd;
        Button btn_kurangd;
        Button btn_tambahd;
        TextView order_quantityd;
        int orderCount = 0;

        public ViewHolder(View view) {
            super(view);
            iv_imgd = view.findViewById(R.id.imgd);
            tv_named = view.findViewById(R.id.named);
            tv_priced = view.findViewById(R.id.priced);
            tv_quantityd = view.findViewById(R.id.quand);
            tv_descriptiond = view.findViewById(R.id.descd);
            btn_kurangd = view.findViewById(R.id.btn_kurangd);
            btn_tambahd = view.findViewById(R.id.btn_tambahd);
            order_quantityd = view.findViewById(R.id.order_quantityd);
        }

        public void bind(DessertModel dessertModel) {
            // Load image using Picasso library
            Picasso.get().load(dessertModel.getImg()).into(iv_imgd);
            tv_named.setText(dessertModel.getName());
            int originalPrice = Integer.parseInt(dessertModel.getPrice());
            int newPrice = originalPrice * 5000;
            tv_priced.setText(String.valueOf(newPrice));
            tv_quantityd.setText(dessertModel.getQuantity());
            tv_descriptiond.setText(dessertModel.getDescription());
            order_quantityd.setText(String.valueOf(orderCount));

            btn_tambahd.setOnClickListener(v -> {
                int maxQuantity = Integer.parseInt(dessertModel.getQuantity());
                if (orderCount < maxQuantity) {
                    orderCount++;
                    order_quantityd.setText(String.valueOf(orderCount));
                    dessertOrderListener.onDessertOrder(dessertModel, orderCount);
                }
            });

            btn_kurangd.setOnClickListener(v -> {
                if (orderCount > 0) {
                    orderCount--;
                    order_quantityd.setText(String.valueOf(orderCount));
                    dessertOrderListener.onDessertOrder(dessertModel, orderCount);
                }
            });
        }
    }
}
