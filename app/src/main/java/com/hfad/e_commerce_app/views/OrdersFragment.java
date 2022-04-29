package com.hfad.e_commerce_app.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.adapters.OrdersAdapter;
import com.hfad.e_commerce_app.models.Order;
import com.hfad.e_commerce_app.token_management.TokenManager;
import com.hfad.e_commerce_app.utils.APIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends Fragment {
    private RecyclerView recyclerViewOrders;
    private OrdersAdapter ordersAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    private TokenManager tokenManager;

    private List<Order> mListOrder = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewOrders = view.findViewById(R.id.recycler_view_orders);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        ordersAdapter = new OrdersAdapter(mListOrder);
        recyclerViewOrders.setAdapter(ordersAdapter);
        recyclerViewOrders.setLayoutManager(new GridLayoutManager(getActivity(),1));

        tokenManager = new TokenManager(getActivity());
        callAPIGetAllOrders();

        ordersAdapter.setItemClickedListener(new OrdersAdapter.ItemClickedListener() {
            @Override
            public void onItemClickedListener(Order order) {
                Intent orderDetailIntent = new Intent(getActivity(), OrderDetailActivity.class);
                orderDetailIntent.putExtra("order", order);
                startActivity(orderDetailIntent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callAPIGetAllOrders();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void callAPIGetAllOrders(){
        APIUtils.getApiServiceInterface().getAllOrders("Bearer "+tokenManager.getAccessToken())
                .enqueue(new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            mListOrder = response.body();
                            ordersAdapter.setmListOrders(mListOrder);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable t) {

                    }
                });
    }


}
