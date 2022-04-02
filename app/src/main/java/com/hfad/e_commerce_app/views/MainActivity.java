package com.hfad.e_commerce_app.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.hfad.e_commerce_app.R;

public class MainActivity extends AppCompatActivity {
    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;

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
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.menu_item_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.menu_item_search:
                        fragment = new SearchFragment();
                        break;
                    case R.id.menu_item_cart:
                        fragment = new CartFragment();
                        break;
                    case R.id.menu_item_orders:
                        fragment = new OrdersFragment();
                        break;
                    case R.id.menu_item_personal:
                        fragment = new PersonalFragment();
                        break;
                }
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