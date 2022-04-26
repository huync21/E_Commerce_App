package com.hfad.e_commerce_app.views;

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
    private NestedScrollView nestedScrollView;

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

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    if(page>totalPage){

                    }
                    else{
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
        nestedScrollView = findViewById(R.id.nested_scroll_view_products_category);
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