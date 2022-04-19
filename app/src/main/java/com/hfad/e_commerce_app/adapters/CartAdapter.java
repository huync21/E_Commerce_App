package com.hfad.e_commerce_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    public interface ItemClickListener {
        void onDeleteCartItem(int cartId);
        void onCartItemQuantityChanged(int productId, int quantity, int cartId);
    }
    private ItemClickListener itemClickListener;

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private List<CartItem> mListCartItems;
    private Context context;

    public CartAdapter(List<CartItem> mListCartItems, Context context) {
        this.mListCartItems = mListCartItems;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = mListCartItems.get(position);
        Glide.with(context).load(cartItem.getProduct().getImage()).into(holder.imageView);
        holder.tvProductName.setText(cartItem.getProduct().getName());
        holder.tvProductPrice.setText("$"+cartItem.getProduct().getPrice()+"");
        holder.edQuantity.setText(cartItem.getQuantity()+"");
        holder.edQuantity.setSelection(holder.edQuantity.getText().length());

        holder.btnDeleteCartItem.setOnClickListener(view -> {
            itemClickListener.onDeleteCartItem(cartItem.getId());
        });
        holder.btnIncrease.setOnClickListener(view -> {
            int quantity = cartItem.getQuantity()+1;
            itemClickListener.onCartItemQuantityChanged(cartItem.getProduct().getId(),quantity, cartItem.getId());
        });
        holder.btnDecrease.setOnClickListener(view -> {
            int quantity = cartItem.getQuantity()-1;
            itemClickListener.onCartItemQuantityChanged(cartItem.getProduct().getId(),quantity, cartItem.getId());
        });



    }

    @Override
    public int getItemCount() {
        if(mListCartItems!=null){
            return mListCartItems.size();
        }
        return 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView tvProductName, tvProductPrice;
        private Button btnDecrease, btnIncrease;
        private EditText edQuantity;
        private ImageButton btnDeleteCartItem;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_cart_item);
            tvProductName = itemView.findViewById(R.id.tv_product_name_cart_item);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price_cart_item);
            btnDecrease = itemView.findViewById(R.id.btn_decrease_cart_item_quantity);
            btnIncrease = itemView.findViewById(R.id.btn_increase_cart_item_quantity);
            edQuantity = itemView.findViewById(R.id.edit_text_cart_item_quantity);
            btnDeleteCartItem = itemView.findViewById(R.id.btn_remove_item_from_cart);
        }
    }

    public void setmListCartItems(List<CartItem> mListCartItems) {
        this.mListCartItems = mListCartItems;
        notifyDataSetChanged();
    }

    public void deleteCartItem(int cartItemId){
        for(int i=0;i<mListCartItems.size();i++){
            CartItem cartItem = mListCartItems.get(i);
            if(cartItem.getId() == cartItemId){
                mListCartItems.remove(cartItem);
                notifyItemRemoved(i);
            }
        }
    }

    public void updateCartItem(int cartItemId,int updatedQuantity){
        for(int i=0;i<mListCartItems.size();i++){
            CartItem cartItem = mListCartItems.get(i);
            if(cartItem.getId() == cartItemId){
                cartItem.setQuantity(updatedQuantity);
                notifyItemChanged(i);
            }
        }
    }
}
