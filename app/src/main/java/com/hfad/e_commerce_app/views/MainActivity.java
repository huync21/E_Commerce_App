package com.hfad.e_commerce_app.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.adapters.MyViewPagerAdapter;
import com.hfad.e_commerce_app.token_management.TokenManager;

public class MainActivity extends AppCompatActivity {
//    private FrameLayout frameLayout;
    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;

    private TokenManager tokenManager;
    String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                tokenManager = new TokenManager(MainActivity.this);
                accessToken = tokenManager.getAccessToken();
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.menu_item_home:
                        viewPager2.setCurrentItem(0,false);
                        break;
                    case R.id.menu_item_search:
                        viewPager2.setCurrentItem(1,false);
                        break;
                    case R.id.menu_item_cart:
                        // Nếu không có accessToken nghĩa là đã logout
                        // => Chuyển hướng đến trang login
                        if (accessToken == null) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            viewPager2.setCurrentItem(2,false);
                        }

                        break;
                    case R.id.menu_item_orders:
                        // Nếu không có accessToken nghĩa là đã logout
                        // => Chuyển hướng đến trang login
                        if (accessToken == null) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            viewPager2.setCurrentItem(3,false);
                        }
                        break;
                    case R.id.menu_item_personal:
                        // Nếu không có accessToken nghĩa là đã logout
                        // => Chuyển hướng đến trang login
                        if (accessToken == null) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            viewPager2.setCurrentItem(4,false);
                        }
                        break;
                }
//                if (fragment != null)
//                    startFragment(fragment);
                return true;
            }
        });

//        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//                tokenManager = new TokenManager(MainActivity.this);
//                accessToken = tokenManager.getAccessToken();
//                switch (position){
//                    case 0:
//                        bottomNavigationView.getMenu().findItem(R.id.menu_item_home).setChecked(true);
//                        break;
//                    case 1:
//                        bottomNavigationView.getMenu().findItem(R.id.menu_item_search).setChecked(true);
//                        break;
//                    case 2:
//                        if (accessToken == null) {
//                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                        }else
//                            bottomNavigationView.getMenu().findItem(R.id.menu_item_cart).setChecked(true);
//                        break;
//                    case 3:
//                        if (accessToken == null) {
//                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                        }
//                        else
//                            bottomNavigationView.getMenu().findItem(R.id.menu_item_orders).setChecked(true);
//                        break;
//                    case 4:
//                        if (accessToken == null) {
//                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                        }else
//                            bottomNavigationView.getMenu().findItem(R.id.menu_item_personal).setChecked(true);
//                        break;
//                }
//            }
//        });

        // disable cai swipe ngang cua viewpager
        viewPager2.setUserInputEnabled(false);

        // set up view pager 2
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),getLifecycle());

        HomeFragment homeFragment = new HomeFragment();
        SearchFragment searchFragment = new SearchFragment();
        CartFragment cartFragment = new CartFragment();
        OrdersFragment ordersFragment = new OrdersFragment();
        PersonalFragment personalFragment = new PersonalFragment();

        myViewPagerAdapter.addFragment(homeFragment);
        myViewPagerAdapter.addFragment(searchFragment);
        myViewPagerAdapter.addFragment(cartFragment);
        myViewPagerAdapter.addFragment(ordersFragment);
        myViewPagerAdapter.addFragment(personalFragment);
        viewPager2.setAdapter(myViewPagerAdapter);


        // Kiem tra xem co phai main activity duoc goi tu DetailProductActivity
        // luc nguoi dung an vao add to cart hay khong, neu co thi dieu huong den
        // cart fragment
        Intent intent = getIntent();
        if(intent !=null){
            int addedToCartCode = intent.getIntExtra(DetailProductActivity.ADDED_TO_CART, 0);
            if(addedToCartCode == DetailProductActivity.ADDED_TO_CART_CODE){
                bottomNavigationView.setSelectedItemId(R.id.menu_item_cart);
            }
            int paymentSuccessCode = intent.getIntExtra(ReviewAndPlaceOrderActivity.PAYMENT_SUCCESS, 0);
            if(paymentSuccessCode == ReviewAndPlaceOrderActivity.PAYMENT_SUCCESS_CODE){
                bottomNavigationView.setSelectedItemId(R.id.menu_item_orders);
            }
        }
    }

    private void initView() {
//        frameLayout = findViewById(R.id.frame_layout_main);
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        viewPager2 = findViewById(R.id.view_pager2_fragment_container);
    }

//    private void startFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .addToBackStack(null)
//                .replace(R.id.frame_layout_main, fragment)
//                .commit();
//
//    }




}