package com.hfad.e_commerce_app.adapters;



import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.Rating;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {
    private List<Rating> mListRating;
    private Activity activity;

    public RatingAdapter(List<Rating> mListRating, Activity activity) {
        this.mListRating = mListRating;
        activity = this.activity;
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating,parent,false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        Rating rating = mListRating.get(position);
        if(rating.getUser().getImage()!=null)
            Glide.with(holder.itemView.getContext()).load(rating.getUser().getImage()).into(holder.circleImageViewUser);
        holder.tvUserFullName.setText(rating.getUser().getFirstName()+" "+rating.getUser().getLastName());
        holder.ratingBar.setRating(rating.getStarNum());
        holder.tvCreatedDate.setText(rating.getCreatedAt());
        holder.tvComment.setText(rating.getComment());
    }

    @Override
    public int getItemCount() {
        if(mListRating!=null)
            return mListRating.size();
        return 0;
    }

    public class RatingViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView circleImageViewUser;
        private TextView tvUserFullName;
        private RatingBar ratingBar;
        private TextView tvCreatedDate;
        private TextView tvComment;
        public RatingViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageViewUser = itemView.findViewById(R.id.circle_image_view_avatar_rating_section);
            tvUserFullName = itemView.findViewById(R.id.tv_user_name_rating_section);
            ratingBar = itemView.findViewById(R.id.rating_bar_of_user_rating_section);
            tvCreatedDate = itemView.findViewById(R.id.tv_created_date_rating_section);
            tvComment = itemView.findViewById(R.id.tv_comment);
        }
    }

    public void setmListRating(List<Rating> listRating){
        this.mListRating = listRating;
        notifyDataSetChanged();
    }
}
