package com.hfad.e_commerce_app.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.Product;
import com.hfad.e_commerce_app.utils.APIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView tvName,tvPrice,tvDesc;
    private RatingBar ratingBar1, ratingBar2;
    private TextView tvAverageStar1, tvRatingNumber1, tvAverageStar2, tvRatingNumber2;
    private ProgressBar progressBar5,progressBar4,progressBar3,progressBar2,progressBar1;
    private Button btnBuy,btnAddToCart;

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
        callAverageStarAPI(productId);

        btnBuy.setOnClickListener(view -> {
            callAverageStarAPI(productId);
        });

    }

    private void initView() {
        imageView = findViewById(R.id.image_view_product_detail);
        tvName = findViewById(R.id.text_view_product_name);
        tvPrice = findViewById(R.id.text_view_product_price);
        tvDesc = findViewById(R.id.text_view_product_description);
        ratingBar1 = findViewById(R.id.rating_bar_detail_product);
        ratingBar2 = findViewById(R.id.rating_bar2);
        tvAverageStar1 = findViewById(R.id.tv_average_star);
        tvRatingNumber1 = findViewById(R.id.tv_rating_number);
        tvRatingNumber2 = findViewById(R.id.tv_rating_number2);
        tvAverageStar2 = findViewById(R.id.tv_average_star2);
        progressBar1 = findViewById(R.id.progress_bar_1star);
        progressBar2 = findViewById(R.id.progress_bar_2star);
        progressBar3 = findViewById(R.id.progress_bar_3star);
        progressBar4 = findViewById(R.id.progress_bar_4star);
        progressBar5 = findViewById(R.id.progress_bar_5star);
        btnBuy = findViewById(R.id.button_buy_product);
        btnAddToCart =findViewById(R.id.button_add_to_cart_product);
    }

    private void callDetailProductApi(int productId){
        APIUtils.getApiServiceInterface().getProductDetail(productId)
                .enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        product = response.body();


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

    private void callAverageStarAPI(int productId){
        APIUtils.getApiServiceInterface().getAverageStarOfProduct(productId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            try {
                                String responseString = response.body().string();
                                JSONObject jsonObject = new JSONObject(responseString);
                                int numberOfRatings = jsonObject.getInt("number_of_ratings");
                                double averageStar = jsonObject.getDouble("average_star");
                                double oneStarRate = jsonObject.getDouble("one_star_rate");
                                double twoStarRate = jsonObject.getDouble("two_star_rate");
                                double threeStarRate = jsonObject.getDouble("three_star_rate");
                                double fourStarRate = jsonObject.getDouble("four_star_rate");
                                double fiveStarRate = jsonObject.getDouble("five_star_rate");

                                ratingBar1.setStepSize(0.01f);
                                ratingBar1.setRating((float) averageStar);
                                ratingBar1.invalidate();
                                tvAverageStar1.setText(averageStar+" stars");
                                tvRatingNumber1.setText("("+numberOfRatings+" ratings)");

                                tvAverageStar2.setText(averageStar+"");
                                ratingBar2.setStepSize(0.01f);
                                ratingBar2.setRating((float) averageStar);
                                ratingBar2.invalidate();
                                tvRatingNumber2.setText(numberOfRatings+"");

                                progressBar1.setProgress((int) Math.round(oneStarRate));
                                progressBar2.setProgress((int) Math.round(twoStarRate));
                                progressBar3.setProgress((int) Math.round(threeStarRate));
                                progressBar4.setProgress((int) Math.round(fourStarRate));
                                progressBar5.setProgress((int) Math.round(fiveStarRate));
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }
}