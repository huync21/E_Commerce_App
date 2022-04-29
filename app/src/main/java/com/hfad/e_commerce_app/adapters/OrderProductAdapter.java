package com.hfad.e_commerce_app.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.e_commerce_app.models.OrderProduct;

import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.OrderProductViewHolder> {
    private List<OrderProduct> mListOrderProducts;

    public OrderProductAdapter(List<OrderProduct> mListOrderProducts) {
        this.mListOrderProducts = mListOrderProducts;
    }


    @NonNull
    @Override
    public OrderProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class OrderProductViewHolder extends RecyclerView.ViewHolder{

        public OrderProductViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
