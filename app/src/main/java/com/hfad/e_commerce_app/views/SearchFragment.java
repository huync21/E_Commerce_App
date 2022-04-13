package com.hfad.e_commerce_app.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.adapters.ProductAdapter;
import com.hfad.e_commerce_app.models.Product;
import com.hfad.e_commerce_app.models.ProductPagination;
import com.hfad.e_commerce_app.utils.APIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;

    private int page=1;
    private int totalPage;
    private String keyword;

    private List<Product> mListProducts = new ArrayList<>();

    private ProductAdapter productAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.search_view_fragment_search);
        recyclerView = view.findViewById(R.id.recycler_view_products_search_fragment);
        nestedScrollView = view.findViewById(R.id.nested_scroll_view_new_products_search_fragment);
        progressBar = view.findViewById(R.id.progress_bar_new_product_search_fragment);

        productAdapter = new ProductAdapter(mListProducts,getActivity());
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                keyword = newText;
                mListProducts.clear();
                callAPIProducts(1,keyword);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    page++;
                    if(page>totalPage){
                        progressBar.setVisibility(View.GONE);
                    }else{
                        progressBar.setVisibility(View.VISIBLE);
                        callAPIProducts(page,keyword);
                    }
                }
            }
        });

        // Xử lý click 1 item
        productAdapter.setItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int productId) {
                Intent intent = new Intent(getActivity(),DetailProductActivity.class);
                intent.putExtra("productId",productId);
                startActivity(intent);
            }
        });
    }

    private void callAPIProducts(int page,String keyword){
        APIUtils.getApiServiceInterface().searchListProducts(keyword,page)
                .enqueue(new Callback<ProductPagination>() {
                    @Override
                    public void onResponse(Call<ProductPagination> call, Response<ProductPagination> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            progressBar.setVisibility(View.GONE);
                            ProductPagination productPagination = response.body();
                            totalPage = productPagination.getTotal_pages();
                            mListProducts.addAll(productPagination.getResults());
                            productAdapter.setmListProduct(mListProducts);
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductPagination> call, Throwable t) {

                    }
                });
    }
}
