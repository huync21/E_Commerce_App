package com.hfad.e_commerce_app.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.adapters.ImagesAdapter;
import com.hfad.e_commerce_app.adapters.RatingAdapter;
import com.hfad.e_commerce_app.models.Image;
import com.hfad.e_commerce_app.models.Product;
import com.hfad.e_commerce_app.models.Rating;
import com.hfad.e_commerce_app.token_management.TokenManager;
import com.hfad.e_commerce_app.utils.APIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private CircleIndicator3 circleIndicator3;
    private TextView tvName,tvPrice,tvDesc;
    private RatingBar ratingBar1, ratingBar2;
    private TextView tvAverageStar1, tvRatingNumber1, tvAverageStar2, tvRatingNumber2;
    private ProgressBar progressBar5,progressBar4,progressBar3,progressBar2,progressBar1;
    private Button btnBuy,btnAddToCart;
    private RecyclerView recyclerView;
    private ImagesAdapter imagesAdapter;
    private RatingAdapter ratingAdapter;
    private TextView tvMakeANewRating;
    private RatingBar ratingBarForUserToRate;
    private Button btnDecreaseQuantity, btnIncreaseQuantity;
    private EditText editTextQuantity;

    private Product product;
    private int productId;
    private List<Rating> ratingList;
    private TokenManager tokenManager;
    private int productQuantity;
    private List<Image> listImages =new ArrayList<>();

    public static int REQUEST_CODE = 2;
    public static String ADDED_TO_CART = "added to cart";
    public static int ADDED_TO_CART_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        initView();
        tokenManager = new TokenManager(this);
        Intent intent = getIntent();
        productId= intent.getIntExtra("productId",0);

        imagesAdapter = new ImagesAdapter(listImages);
        viewPager2.setAdapter(imagesAdapter);
        circleIndicator3.setViewPager(viewPager2);

        ratingAdapter = new RatingAdapter(ratingList, DetailProductActivity.this);
        recyclerView.setAdapter(ratingAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(DetailProductActivity.this,1));

        callDetailProductApi(productId);
        callAverageStarAPI(productId);
        callAPIRatings(productId);

        tvMakeANewRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int starNum = (int)ratingBarForUserToRate.getRating();
                if(checkIfUserHasLogin()) {
                    navigateToMakeARatingActivity(starNum);
                }
                else{
                    Toast.makeText(DetailProductActivity.this, "Please login to make a rating.", Toast.LENGTH_SHORT).show();
                    navigateToLoginActivity();
                }
            }
        });

        ratingBarForUserToRate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int starNum = (int) v;
                if(checkIfUserHasLogin()) {
                    navigateToMakeARatingActivity(starNum);
                }
                else{
                    Toast.makeText(DetailProductActivity.this, "Please login to make a rating.", Toast.LENGTH_SHORT).show();
                    navigateToLoginActivity();
                }
            }
        });


        // tinh nang add to cart
        editTextQuantity.setSelection(editTextQuantity.getText().length());

        productQuantity = Integer.parseInt(editTextQuantity.getText().toString());
        btnDecreaseQuantity.setOnClickListener(view -> {
            String input = editTextQuantity.getText().toString();
            if(!input.isEmpty()){
                productQuantity = Integer.parseInt(editTextQuantity.getText().toString());
                if(productQuantity >0){
                    productQuantity-=1;
                    editTextQuantity.setText(productQuantity+"");
                    editTextQuantity.setSelection(editTextQuantity.getText().length());
                }
            }


        });
        btnIncreaseQuantity.setOnClickListener(view -> {
            String input = editTextQuantity.getText().toString();
            if(!input.isEmpty()){
                productQuantity = Integer.parseInt(editTextQuantity.getText().toString());
                productQuantity++;
                editTextQuantity.setText(productQuantity+"");
                editTextQuantity.setSelection(editTextQuantity.getText().length());
            }


        });


        btnAddToCart.setOnClickListener(view -> {
            callAPIAddToCartOrUpdate(productId,productQuantity);
        });


    }

    private boolean checkIfUserHasLogin(){
        if(tokenManager.getAccessToken()==null){
            return false;
        }
        return true;
    }

    private void navigateToLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    private void navigateToMakeARatingActivity(int starNum){
        Intent intent = new Intent(this, MakeARatingActivity.class);
        intent.putExtra("starNum", starNum);
        intent.putExtra("product",product);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {
            callAPIRatings(productId);
            callAverageStarAPI(productId);
//            ratingBarForUserToRate.setRating(0);
//            ratingBarForUserToRate.invalidate();
        }
    }

    private void initView() {
        viewPager2 = findViewById(R.id.view_pager_2_product_detail);
        circleIndicator3 = findViewById(R.id.circle_indicator_3_image_product_detail);
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
        recyclerView = findViewById(R.id.recycler_view_rating);
        tvMakeANewRating = findViewById(R.id.tv_leave_your_rating);
        ratingBarForUserToRate = findViewById(R.id.rating_bar_user_rate);
        btnDecreaseQuantity = findViewById(R.id.btn_decrease_cart_item_quantity);
        btnIncreaseQuantity = findViewById(R.id.btn_increase_cart_item_quantity);
        editTextQuantity = findViewById(R.id.edit_text_cart_item_quantity);
    }

    private void callDetailProductApi(int productId){
        APIUtils.getApiServiceInterface().getProductDetail(productId)
                .enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        product = response.body();
                        listImages.add(new Image(product.getImage()));
                        callGetAllImageOfProductAPI(product.getId());
                        tvName.setText(product.getName());
                        tvPrice.setText("$"+product.getPrice());
                        tvDesc.setText(product.getDescription());
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {

                    }
                });
    }

    private void callGetAllImageOfProductAPI(int productId){
        APIUtils.getApiServiceInterface().getImagesOfProduct(productId)
                .enqueue(new Callback<List<Image>>() {
                    @Override
                    public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            List<Image> result = response.body();
                            listImages.addAll(result);
                            imagesAdapter.setmListImage(listImages);
                            viewPager2.setAdapter(imagesAdapter);
                            circleIndicator3.setViewPager(viewPager2);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Image>> call, Throwable t) {

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

    private void callAPIRatings(int productId){
        APIUtils.getApiServiceInterface().getAllRatingsOfProduct(productId)
                .enqueue(new Callback<List<Rating>>() {
                    @Override
                    public void onResponse(Call<List<Rating>> call, Response<List<Rating>> response) {
                        if(response.isSuccessful() && response.body()!=null){
                             ratingList = response.body();
                             ratingAdapter = new RatingAdapter(ratingList, DetailProductActivity.this);
                             recyclerView.setAdapter(ratingAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Rating>> call, Throwable t) {

                    }
                });
    }

    private void callAPIAddToCartOrUpdate(int productId, int productQuantity){
        APIUtils.getApiServiceInterface().createOrUpdateCartItem("Bearer "+tokenManager.getAccessToken(),productQuantity,productId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            try {
                                String jsonStringResponse = response.body().string();
                                JSONObject jsonObject = new JSONObject(jsonStringResponse);
                                String message = jsonObject.getString("message");
                                Toast.makeText(DetailProductActivity.this, message, Toast.LENGTH_SHORT).show();

                                Intent intent1 = new Intent(DetailProductActivity.this, MainActivity.class);
                                intent1.putExtra(ADDED_TO_CART,ADDED_TO_CART_CODE);
                                startActivity(intent1);
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }




}