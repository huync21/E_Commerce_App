package com.hfad.e_commerce_app.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.token_management.TokenManager;

public class MainActivity extends AppCompatActivity {
    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;
    private TokenManager tokenManager;
    String accessToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();


        //Lúc mới vào thì hiện home fragment
        startFragment(new HomeFragment());
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                tokenManager = new TokenManager(MainActivity.this);
                accessToken = tokenManager.getAccessToken();
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.menu_item_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.menu_item_search:
                        fragment = new SearchFragment();
                        break;
                    case R.id.menu_item_cart:
                        // Nếu không có accessToken nghĩa là đã logout
                        // => Chuyển hướng đến trang login
                        if(accessToken == null){
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }else{
                            fragment = new CartFragment();
                        }

                        break;
                    case R.id.menu_item_orders:
                        // Nếu không có accessToken nghĩa là đã logout
                        // => Chuyển hướng đến trang login
                        if(accessToken == null){
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }else{
                            fragment = new OrdersFragment();
                        }
                        break;
                    case R.id.menu_item_personal:
                        // Nếu không có accessToken nghĩa là đã logout
                        // => Chuyển hướng đến trang login
                        if(accessToken == null){
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }else{
                            fragment = new PersonalFragment();
                        }

                        break;
                }
                if(fragment!=null)
                    startFragment(fragment);
                return true;
            }
        });
    }

    private void initView() {
        frameLayout = findViewById(R.id.frame_layout_main);
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
    }

    private void startFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout_main,fragment);
        transaction.commit();
    }

}