package com.hfad.e_commerce_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.Order;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Order> mListOrders;

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;

    public OrdersAdapter(List<Order> mListOrders) {
        this.mListOrders = mListOrders;
    }

    class OrderViewHolder extends RecyclerView.ViewHolder{
        private TextView tvOrderTotal, tvCreatedDate, tvStatus;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderTotal = itemView.findViewById(R.id.tv_order_total_item_order);
            tvCreatedDate = itemView.findViewById(R.id.tv_created_at_item_order);
            tvStatus = itemView.findViewById(R.id.tv_status_item_order);
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder{
        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            this.progressBar = itemView.findViewById(R.id.my_progress_bar);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mListOrders.get(position) == null ? TYPE_LOADING : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order,parent,false);
            return new OrderViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_progress_bar,parent,false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == TYPE_ITEM){
            Order order = mListOrders.get(position);
            OrderViewHolder orderViewHolder = (OrderViewHolder) holder;
            orderViewHolder.tvOrderTotal.setText("Total: "+order.getOrderTotal()+"$");
            orderViewHolder.tvCreatedDate.setText("Created at: "+order.getModified_at());

            switch (order.getStatus()) {
                    case "Delivering":
                        orderViewHolder.tvStatus.setTextColor(orderViewHolder.itemView.getContext().getResources().getColor(R.color.purple_500));
                        break;
                    case "Completed":
                        orderViewHolder.tvStatus.setTextColor(orderViewHolder.itemView.getContext().getResources().getColor(R.color.teal_700));
                        break;
                    case "Canceled":
                        orderViewHolder.tvStatus.setTextColor(orderViewHolder.itemView.getContext().getResources().getColor(R.color.red));

                        break;
            }
            orderViewHolder.tvStatus.setText("Status: "+order.getStatus());
            orderViewHolder.itemView.setOnClickListener(view -> {
                itemClickedListener.onItemClickedListener(order);
            });
        }
    }

    @Override
    public int getItemCount() {
        if(mListOrders!=null)
            return mListOrders.size();
        return 0;
    }



    public interface ItemClickedListener{
        void onItemClickedListener(Order order);
    }

    private ItemClickedListener itemClickedListener;

    public void setItemClickedListener(ItemClickedListener itemClickedListener) {
        this.itemClickedListener = itemClickedListener;
    }

    public void setmListOrders(List<Order> mListOrders) {
        this.mListOrders = mListOrders;
        notifyDataSetChanged();
    }

    public void addLoadingEffect(){
        mListOrders.add(null);
        notifyItemInserted(mListOrders.size()-1);
    }

    public void removeLoadingEffect(){
        int position = mListOrders.size()-1;
        Order order = mListOrders.get(position);
        if(order==null){
            mListOrders.remove(position);
            notifyDataSetChanged();
        }
    }
}
