package com.hfad.e_commerce_app.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.User;
import com.hfad.e_commerce_app.utils.APIUtils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtEmail, txtUsername, txtFirstName, txtLastName, txtPassword;
    private Button btnRegister;
    private TextView txtBackToLoginPage;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        btnRegister.setOnClickListener(view -> {
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.show();
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
                        progressDialog.cancel();
                        if(response.isSuccessful() && response.body()!=null){
                            if(response.isSuccessful() && response!=null) {
                                Intent intent = getIntent();
                                intent.putExtra("key", "Đã gửi mail xác nhận tài khoản tại địa chỉ email: "+response.body().getEmail()+" mời bạn check mail xác nhận tài khoản");
                                intent.putExtra("email",response.body().getEmail());
                                setResult(RESULT_OK, intent);
                                finish();
                            }else{
                                try {
                                   String errorMessage = response.errorBody().string();

                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setTitle("Lỗi Đăng Ký")
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
        txtLastName = findViewById(R.id.text_view_lastname_register);
        txtPassword = findViewById(R.id.text_view_password_register);
        btnRegister = findViewById(R.id.btn_register);
        txtBackToLoginPage = findViewById(R.id.text_view_go_back_login_page);
    }
}