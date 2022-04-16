package com.hfad.e_commerce_app.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.JWTToken;
import com.hfad.e_commerce_app.token_management.TokenManager;
import com.hfad.e_commerce_app.utils.APIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextView editTextEmail, editTextPassword;
    private Button btnLogin;
    private TextView txtRegister;
    private TextView txtErrorEmail, txtErrorPassword;
    private ProgressBar progressBar;

    // Token manager để lưu access token vào SharedPreference
    private TokenManager tokenManager;
    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        tokenManager = new TokenManager(this);
        btnLogin.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            disableGUI();
            callLoginApi();
        });

        txtRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    private void initView() {
        editTextEmail = findViewById(R.id.edit_text_email_login);
        editTextPassword = findViewById(R.id.edit_text_password_login);
        btnLogin = findViewById(R.id.btn_login);
        txtRegister = findViewById(R.id.text_view_go_to_register);
        txtErrorEmail = findViewById(R.id.text_view_error_email_login);
        progressBar = findViewById(R.id.progress_bar_login_activity);
    }

    private void callLoginApi() {
        APIUtils.getApiServiceInterface()
                .login(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                .enqueue(new Callback<JWTToken>() {
                    @Override
                    public void onResponse(Call<JWTToken> call, Response<JWTToken> response) {
                        enableGUI();
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response != null) {
                            Log.d("RESPONSE Login Activity", response.body().toString());
                            String message = response.body().toString();
                            // Đăng nhập thành công thì lưu token vào share preference thông qua lớp TokenManager mà
                            // mình đã viết
                            JWTToken jwtToken = response.body();
                            tokenManager.saveUserInfo(jwtToken.getEmail(), jwtToken.getFirstName()
                                    , jwtToken.getLastName(), jwtToken.getPhone(), jwtToken.getAccessToken(), jwtToken.getRefreshToken(), jwtToken.getImage());
                            returnToMainActivity();
                        } else {
                            // Nếu đăng nhập thất bại thì hiển thị ra khung message lỗi
                            try {
                                String errorMessage = response.errorBody().string();
//                                JSONObject errorJson = new JSONObject(errorMessage);
//                                Iterator<String> iter = errorJson.keys();
//                                while (iter.hasNext()) {
//                                    String key = iter.next();

//                                }
//                                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle("Lỗi Đăng Nhập")
                                        .setMessage(errorMessage)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        })
                                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialogInterface) {
                                                dialogInterface.cancel();
                                            }
                                        })
                                        .show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JWTToken> call, Throwable t) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        returnToMainActivity();
        super.onBackPressed();
    }

    private void returnToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void disableGUI() {
        editTextEmail.setEnabled(false);
        editTextPassword.setEnabled(false);
        btnLogin.setEnabled(false);
        txtRegister.setEnabled(false);
    }

    private void enableGUI() {
        editTextEmail.setEnabled(true);
        editTextPassword.setEnabled(true);
        btnLogin.setEnabled(true);
        txtRegister.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {
                String userEmail = data.getStringExtra("email");
                editTextEmail.setText(userEmail);
                String messageFromRegisterActivity = data.getStringExtra("key");
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Thông báo xác nhận email")
                        .setMessage(messageFromRegisterActivity)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();

            }
        } catch (Exception ex) {
            Toast.makeText(LoginActivity.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }
}