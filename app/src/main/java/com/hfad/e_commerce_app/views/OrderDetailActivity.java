package com.hfad.e_commerce_app.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.adapters.OrderProductAdapter;
import com.hfad.e_commerce_app.models.Order;
import com.hfad.e_commerce_app.models.OrderProduct;
import com.hfad.e_commerce_app.token_management.TokenManager;
import com.hfad.e_commerce_app.utils.APIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView tvStatus, tvShipTo, tvSubtotal, tvShipping, tvTax, tvOrderTotal;
    private ImageView imageViewShipment, imageViewPayment;
    private RecyclerView recyclerView;

    private OrderProductAdapter orderProductAdapter;

    private TokenManager tokenManager;
    private List<OrderProduct> mListOrderProducts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();
        tokenManager = new TokenManager(this);

        Intent intentFromOrderFragment = getIntent();
        Order order = (Order) intentFromOrderFragment.getSerializableExtra("order");

        tvStatus.setText(order.getStatus());
        tvShipTo.setText(order.getShippingAddress());
        tvSubtotal.setText("$"+order.getTotalPrice()+"");
        tvShipping.setText("$"+order.getShippingPrice());
        tvOrderTotal.setText("$"+order.getOrderTotal()+"");

        Glide.with(this).load(order.getPayment().getImage()).into(imageViewPayment);
        Glide.with(this).load(order.getShipment().getImage()).into(imageViewShipment);

        orderProductAdapter = new OrderProductAdapter(mListOrderProducts);
        recyclerView.setAdapter(orderProductAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));

        callAPIOrderDetails(order.getId());

    }

    private void initView(){
        recyclerView = findViewById(R.id.recyclerViewCartItems);
        tvShipTo= findViewById(R.id.tvShipto);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShipping = findViewById(R.id.tvShipping);
        tvTax = findViewById(R.id.tvTax);
        tvOrderTotal = findViewById(R.id.tvOrderTotal);
        imageViewPayment = findViewById(R.id.imageViewPayWith);
        imageViewShipment = findViewById(R.id.imageViewShipment2);
        tvStatus = findViewById(R.id.tvStatus);
    }

    private void callAPIOrderDetails(int orderId){
        APIUtils.getApiServiceInterface().getOrderDetail("Bearer "+ tokenManager.getAccessToken(),orderId)
                .enqueue(
                        new Callback<List<OrderProduct>>() {
                            @Override
                            public void onResponse(Call<List<OrderProduct>> call, Response<List<OrderProduct>> response) {
                                if(response.isSuccessful() && response.body()!=null){
                                    mListOrderProducts= response.body();
                                    orderProductAdapter.setmListOrderProducts(mListOrderProducts);
                                }
                            }

                            @Override
                            public void onFailure(Call<List<OrderProduct>> call, Throwable t) {

                            }
                        }
                );
    }


}