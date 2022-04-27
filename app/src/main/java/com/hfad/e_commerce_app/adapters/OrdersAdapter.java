package com.hfad.e_commerce_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.Order;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    private List<Order> mListOrders;

    public OrdersAdapter(List<Order> mListOrders) {
        this.mListOrders = mListOrders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order,parent,false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = mListOrders.get(position);
        holder.tvOrderTotal.setText("Order total: "+order.getOrderTotal()+"$");
        holder.tvCreatedDate.setText("Created at: "+order.getCreatedAt());
        holder.tvStatus.setText("Status: "+order.getStatus());
    }

    @Override
    public int getItemCount() {
        if(mListOrders!=null)
            return mListOrders.size();
        return 0;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{
        private TextView tvOrderTotal, tvCreatedDate, tvStatus;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderTotal = itemView.findViewById(R.id.tv_order_total_item_order);
            tvCreatedDate = itemView.findViewById(R.id.tv_created_at_item_order);
            tvStatus = itemView.findViewById(R.id.tv_status_item_order);
        }
    }

    public interface ItemClickedListener{
        void onItemClickedListener(int position);
    }

    private ItemClickedListener itemClickedListener;

    public void setItemClickedListener(ItemClickedListener itemClickedListener) {
        this.itemClickedListener = itemClickedListener;
    }

    public void setmListOrders(List<Order> mListOrders) {
        this.mListOrders = mListOrders;
        notifyDataSetChanged();
    }
}
