package com.hfad.e_commerce_app.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.Product;
import com.hfad.e_commerce_app.utils.APIUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView tvName,tvPrice,tvDesc;

    private Product product;
    private int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        initView();
        Intent intent = getIntent();
        productId= intent.getIntExtra("productId",0);

        callDetailProductApi(productId);
    }

    private void initView() {
        imageView = findViewById(R.id.image_view_product_detail);
        tvName = findViewById(R.id.text_view_product_name);
        tvPrice = findViewById(R.id.text_view_product_price);
        tvDesc = findViewById(R.id.text_view_product_description);
    }

    private void callDetailProductApi(int productId){
        APIUtils.getApiServiceInterface().getProductDetail(productId)
                .enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        Product product = response.body();
                        Glide.with(DetailProductActivity.this).load(product.getImage())
                                .into(imageView);
                        tvName.setText(product.getName());
                        tvPrice.setText("$"+product.getPrice());
                        tvDesc.setText(product.getDescription());
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {

                    }
                });
    }
}