package com.hfad.e_commerce_app.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.JWTToken;
import com.hfad.e_commerce_app.utils.APIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextView txtEmail, txtPassword;
    private Button btnLogin;
    private TextView txtRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        btnLogin.setOnClickListener(view -> callLoginApi());

        txtRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void initView() {
        txtEmail = findViewById(R.id.text_view_email_login);
        txtPassword = findViewById(R.id.text_view_password_login);
        btnLogin = findViewById(R.id.btn_login);
        txtRegister = findViewById(R.id.text_view_go_to_register);
    }

    private void callLoginApi(){
        APIUtils.getApiServiceInterface()
                .login(txtEmail.getText().toString(),txtPassword.getText().toString())
                .enqueue(new Callback<JWTToken>() {
                    @Override
                    public void onResponse(Call<JWTToken> call, Response<JWTToken> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            Toast.makeText(LoginActivity.this,response.body().toString(),Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<JWTToken> call, Throwable t) {

                    }
                });
    }

}