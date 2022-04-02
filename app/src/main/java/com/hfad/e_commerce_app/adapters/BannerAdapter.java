package com.hfad.e_commerce_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.Banner;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
    private List<Banner> mListBanner;

    public BannerAdapter(List<Banner> mListBanner) {
        this.mListBanner = mListBanner;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner,parent,false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Banner banner = mListBanner.get(position);
        if(banner == null){
            return;
        }
        // Tí nữa sửa thành Glide hiển thị ảnh từ url
        holder.img.setImageResource(banner.getImageIdTest());
    }

    @Override
    public int getItemCount() {
        if(mListBanner != null){
            return mListBanner.size();
        }
        return 0;
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_banner);
        }
    }
}
