package com.hfad.e_commerce_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.Category;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private List<Category> mListCategory;

    public CategoryAdapter(List<Category> mListCategory) {
        this.mListCategory = mListCategory;
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
            // Tí nữa phải sửa thành sử dụng Glide load ảnh từ URL
            holder.circleImageView.setImageResource(category.getImageTest());
            holder.textView.setText(category.getName());
        }
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
}
