package com.hfad.e_commerce_app.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfad.e_commerce_app.R;
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

    private TokenManager tokenManager;
    private List<OrderProduct> mListOrderProducts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();

        tokenManager = new TokenManager(this);

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
                                    mListOrderProducts.addAll(response.body());

                                }
                            }

                            @Override
                            public void onFailure(Call<List<OrderProduct>> call, Throwable t) {

                            }
                        }
                );
    }
}