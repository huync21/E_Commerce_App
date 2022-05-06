package com.hfad.e_commerce_app.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.adapters.ProductAdapter;
import com.hfad.e_commerce_app.models.Category;
import com.hfad.e_commerce_app.models.Product;
import com.hfad.e_commerce_app.models.ProductPagination;
import com.hfad.e_commerce_app.utils.APIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsByCategoryActivity extends AppCompatActivity {
    private TextView tvCategory;
    private RecyclerView recyclerView;

    private ProductAdapter productAdapter;

    private int page=1;
    private int totalPage;

    private List<Product> mListProducts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_by_category);
        initView();
        productAdapter = new ProductAdapter(mListProducts, this);
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        Intent intent = getIntent();
        Category category = (Category) intent.getSerializableExtra("category");

        tvCategory.setText(category.getName());
        callAPIGetProductByCategory(category.getName(), 1);


        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE){
                        if(page<=totalPage){
                            callAPIGetProductByCategory(category.getName(), page);
                        }

                }
            }
        });

        productAdapter.setItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int productId) {
                Intent intent = new Intent(ProductsByCategoryActivity.this,DetailProductActivity.class);
                intent.putExtra("productId",productId);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        tvCategory = findViewById(R.id.textView_category_product_by_category);
        recyclerView = findViewById(R.id.recycler_view_products_by_category);
    }

    private void callAPIGetProductByCategory(String category, int page){
        APIUtils.getApiServiceInterface().getListProductsByCategory(category,page)
                .enqueue(new Callback<ProductPagination>() {
                    @Override
                    public void onResponse(Call<ProductPagination> call, Response<ProductPagination> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            ProductPagination productPagination = response.body();
                            ProductsByCategoryActivity.this.totalPage = productPagination.getTotal_pages();
                            mListProducts.addAll(productPagination.getResults());
                            productAdapter.setmListProduct(mListProducts);

                            ProductsByCategoryActivity.this.page++;
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductPagination> call, Throwable t) {

                    }
                });
    }
}