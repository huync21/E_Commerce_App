package com.hfad.e_commerce_app.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.Product;
import com.hfad.e_commerce_app.models.Rating;
import com.hfad.e_commerce_app.models.User;
import com.hfad.e_commerce_app.token_management.TokenManager;
import com.hfad.e_commerce_app.utils.APIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeARatingActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView tvProductName;
    private CircleImageView circleImageView;
    private TextView tvUserFullName;
    private RatingBar ratingBar;
    private Button btnRate;
    private EditText editText;

    private TokenManager tokenManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_arating);
        initView();
        tokenManager = new TokenManager(this);

        Intent intent = getIntent();
        int starNum = intent.getIntExtra("starNum", 0);
        Product product = (Product) intent.getSerializableExtra("product");

        if(product != null){
            Glide.with(this).load(product.getImage()).into(imageView);
            tvProductName.setText(product.getName());
        }

        ratingBar.setRating(starNum);
        callAPIGetUserInfo();

        btnRate.setOnClickListener(view -> {
            //hide keyboard
            InputMethodManager systemService = (InputMethodManager) MakeARatingActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            systemService.hideSoftInputFromWindow(view.getWindowToken(),0);

            int starNumber = (int) ratingBar.getRating();
            String comment = editText.getText().toString();
            callCreateRatingAPI(starNumber,comment, product.getId());
        });



    }


    private void callCreateRatingAPI(int starNum, String comment, int productId) {
        APIUtils.getApiServiceInterface().createRating("Bearer "+tokenManager.getAccessToken(), starNum, comment, productId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            try {
                                String data = response.body().string();
                                JSONObject jsonObject = new JSONObject(data);
                                String message = jsonObject.getString("message");
                                String ratingString = jsonObject.getJSONObject("rating").toString();
                                Gson gson = new Gson();
                                Rating rating = gson.fromJson(ratingString,Rating.class);

                                Toast.makeText(MakeARatingActivity.this, message, Toast.LENGTH_SHORT).show();
                                Intent intent = getIntent();
                                setResult(RESULT_OK, intent);
                                finish();
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

    private void callAPIGetUserInfo(){
        APIUtils.getApiServiceInterface().getUserInfo("Bearer "+tokenManager.getAccessToken())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            User user = response.body();
                            Glide.with(MakeARatingActivity.this).load(user.getImage()).into(circleImageView);
                            tvUserFullName.setText(user.getFirstName()+" "+user.getLastName());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
    }

    private void initView() {
        imageView = findViewById(R.id.imageView_product_image_activity_make_a_rating);
        tvProductName = findViewById(R.id.tv_product_name_activity_make_a_rating);
        circleImageView = findViewById(R.id.circle_image_view_avatar_activity_make_a_rating);
        tvUserFullName = findViewById(R.id.tv_user_full_name_activity_make_a_rating);
        ratingBar = findViewById(R.id.ratingBar_activity_make_a_rating);
        btnRate = findViewById(R.id.btn_make_a_rating_activity_make_a_rating);
        editText = findViewById(R.id.edit_text_make_comment_for_rating);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            View view = getCurrentFocus();
            if(view instanceof EditText){
                Rect rect = new Rect();
                view.getGlobalVisibleRect(rect);
                if(!rect.contains((int)ev.getRawX(),(int)ev.getRawX())){
                    InputMethodManager systemService = (InputMethodManager) MakeARatingActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    systemService.hideSoftInputFromWindow(view.getWindowToken(),0);
                    view.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}