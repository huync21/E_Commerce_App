package com.hfad.e_commerce_app.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.adapters.OrdersAdapter;
import com.hfad.e_commerce_app.models.Order;
import com.hfad.e_commerce_app.models.OrderPagination;
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
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button btnStatistic;
    private TokenManager tokenManager;
    private int page=1;
    private int totalPage;
    private boolean isLoading;

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
        btnStatistic = view.findViewById(R.id.btnStatistic);
        ordersAdapter = new OrdersAdapter(mListOrder);
        recyclerViewOrders.setAdapter(ordersAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewOrders.setLayoutManager(linearLayoutManager);

        recyclerViewOrders.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    if(page<=totalPage)
                        callAPIGetOrderWithPagination(page);
                }
            }
        });

        tokenManager = new TokenManager(getActivity());

        callAPIGetOrderWithPagination(page);

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
                if(isLoading)
                    ordersAdapter.removeLoadingEffect();
                page = 1;
                mListOrder= new ArrayList<>();
                ordersAdapter.setmListOrders(mListOrder);
                callAPIGetOrderWithPagination(page);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        btnStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGoToStatisticActivity = new Intent(getActivity(),StatisticActivity.class);
                startActivity(intentGoToStatisticActivity);
            }
        });
    }


    private void callAPIGetOrderWithPagination(int page){
        if(page<=totalPage && !isLoading){
            ordersAdapter.addLoadingEffect();
            isLoading = true;
        }
        APIUtils.getApiServiceInterface().getAllOrdersWithPagination("Bearer "+tokenManager.getAccessToken(), page)
                .enqueue(new Callback<OrderPagination>() {
                    @Override
                    public void onResponse(Call<OrderPagination> call, Response<OrderPagination> response) {
                        if(isLoading){
                            ordersAdapter.removeLoadingEffect();
                            isLoading=false;
                        }
                        if(response.isSuccessful() && response.body()!=null){
                            OrderPagination orderPagination = response.body();
                            List<Order> listOrder = orderPagination.getResults();
                            mListOrder.addAll(listOrder);
                            totalPage = orderPagination.getTotal_pages();
                            ordersAdapter.setmListOrders(mListOrder);

                            OrdersFragment.this.page++;

                        }
                    }

                    @Override
                    public void onFailure(Call<OrderPagination> call, Throwable t) {
                        if(page<totalPage)
                            ordersAdapter.removeLoadingEffect();
                    }
                });
    }

}
