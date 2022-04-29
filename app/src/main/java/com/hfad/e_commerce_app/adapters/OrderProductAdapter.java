package com.hfad.e_commerce_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.CartItem;
import com.hfad.e_commerce_app.models.Order;
import com.hfad.e_commerce_app.models.OrderProduct;

import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.OrderProductViewHolder> {
    private List<OrderProduct> mListOrderProducts;

    public OrderProductAdapter(List<OrderProduct> mListOrderProducts) {
        this.mListOrderProducts = mListOrderProducts;
    }

    public void setmListOrderProducts(List<OrderProduct> mListOrderProducts) {
        this.mListOrderProducts = mListOrderProducts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_review_order, parent, false);
        return new OrderProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductViewHolder holder, int position) {
        OrderProduct orderProduct = mListOrderProducts.get(position);
        Glide.with(holder.itemView.getContext()).load(orderProduct.getProduct().getImage()).into(holder.imageView);
        holder.tvProductName.setText(orderProduct.getProduct().getName());
        holder.tvQuantity.setText(orderProduct.getQuantity() + " x ");
        holder.tvPrice.setText("$" + orderProduct.getProduct().getPrice() + "");
    }

    @Override
    public int getItemCount() {
        if(mListOrderProducts!=null)
            return mListOrderProducts.size();
        return 0;
    }

    public class OrderProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView tvProductName, tvQuantity, tvPrice;

        public OrderProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewCartItemReviewOrder);
            tvProductName = itemView.findViewById(R.id.tvProductNameReviewOrder);
            tvQuantity = itemView.findViewById(R.id.tvQuantityReviewOrder);
            tvPrice = itemView.findViewById(R.id.tvPriceReviewOrder);
        }
    }
}
