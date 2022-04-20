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

import java.util.List;

public class ReviewOrderAdapter extends RecyclerView.Adapter<ReviewOrderAdapter.ReviewOrderViewHolder> {
    private List<CartItem> cartItemList;

    public ReviewOrderAdapter(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public ReviewOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_review_order,parent,false);
        return new ReviewOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewOrderViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        Glide.with(holder.itemView.getContext()).load(cartItem.getProduct().getImage()).into(holder.imageView);
        holder.tvProductName.setText(cartItem.getProduct().getName());
        holder.tvQuantity.setText(cartItem.getQuantity()+" x ");
        holder.tvPrice.setText("$"+cartItem.getProduct().getPrice()+"");
    }

    @Override
    public int getItemCount() {
        if(cartItemList!=null)
            return cartItemList.size();
        return 0;
    }

    public class ReviewOrderViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView tvProductName, tvQuantity, tvPrice;
        public ReviewOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewCartItemReviewOrder);
            tvProductName = itemView.findViewById(R.id.tvProductNameReviewOrder);
            tvQuantity = itemView.findViewById(R.id.tvQuantityReviewOrder);
            tvPrice = itemView.findViewById(R.id.tvPriceReviewOrder);
        }
    }
}
