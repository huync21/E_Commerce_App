package com.hfad.e_commerce_app.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.Category;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private List<Category> mListCategory;
    private Activity activity;

    public CategoryAdapter(List<Category> mListCategory, Activity activity) {
        this.mListCategory = mListCategory;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = mListCategory.get(position);
        if(category!=null){
            // Glide load ảnh để hiển thị ra
            Glide.with(activity).load(category.getImage()).into(holder.circleImageView);
            holder.textView.setText(category.getName());
        }
        holder.itemView.setOnClickListener(view -> {
            itemClickedListener.onItemClicked(mListCategory.get(holder.getAdapterPosition()));
        });
    }

    @Override
    public int getItemCount() {
        if(mListCategory !=null) return mListCategory.size();
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView circleImageView;
        private TextView textView;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.circle_image_view_category_item);
            textView = itemView.findViewById(R.id.text_view_category_item);
        }
    }

    public void setmListCategory(List<Category> mListCategory){
        this.mListCategory = mListCategory;
        notifyDataSetChanged();
    }

    public interface ItemClickedListener{
        void onItemClicked(Category category);
    }

    public ItemClickedListener itemClickedListener;

    public void setItemClickedListener(ItemClickedListener itemClickedListener) {
        this.itemClickedListener = itemClickedListener;
    }
}
