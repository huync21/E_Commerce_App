package com.hfad.e_commerce_app.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.User;
import com.hfad.e_commerce_app.utils.APIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private TextView txtEmail, txtUsername, txtFirstName, txtLastName, txtPassword;
    private Button btnRegister;
    private TextView txtBackToLoginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        btnRegister.setOnClickListener(view -> {
            callRegisterApi();
        });
        txtBackToLoginPage.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    private void callRegisterApi() {
        APIUtils.getApiServiceInterface().register(txtEmail.getText().toString(),
                txtPassword.getText().toString(), txtUsername.getText().toString()
                , txtFirstName.getText().toString(), txtLastName.getText().toString())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            Toast.makeText(RegisterActivity.this, response.body().toString(),Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
    }

    private void initView() {
        txtEmail = findViewById(R.id.text_view_email_register);
        txtUsername = findViewById(R.id.text_view_username_register);
        txtFirstName = findViewById(R.id.text_view_firstname_register);
        txtPassword = findViewById(R.id.text_view_password_register);
        btnRegister = findViewById(R.id.btn_register);
        txtBackToLoginPage = findViewById(R.id.text_view_go_back_login_page);
    }
}