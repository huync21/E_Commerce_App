package com.hfad.e_commerce_app.adapters;



import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> mListProduct;
    private Activity activity;

    public ProductAdapter(List<Product> mListProduct, Activity activity) {
        this.mListProduct = mListProduct;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = mListProduct.get(position);
        if(product != null){
            // Glide load ảnh từ url vào image view
            Glide.with(activity).load(product.getImage()).into(holder.imageView);
            holder.textViewName.setText(product.getName());
            holder.textViewPrice.setText("$"+product.getPrice());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(product.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListProduct !=null)
            return mListProduct.size();
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textViewName, textViewPrice;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_product);
            textViewName = itemView.findViewById(R.id.text_view_product_name);
            textViewPrice = itemView.findViewById(R.id.text_view_product_price);
        }
    }

    public void setmListProduct(List<Product> mListProduct){
        this.mListProduct = mListProduct;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClick(int productId);
    }
    private OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
