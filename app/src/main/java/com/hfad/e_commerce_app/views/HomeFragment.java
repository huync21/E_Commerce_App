package com.hfad.e_commerce_app.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.adapters.BannerAdapter;
import com.hfad.e_commerce_app.adapters.CategoryAdapter;
import com.hfad.e_commerce_app.adapters.ProductAdapter;
import com.hfad.e_commerce_app.models.Banner;
import com.hfad.e_commerce_app.models.Category;
import com.hfad.e_commerce_app.models.Product;
import com.hfad.e_commerce_app.models.ProductPagination;
import com.hfad.e_commerce_app.retrofit.APIServiceInterface;
import com.hfad.e_commerce_app.utils.APIUtils;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    // View components
    private ViewPager2 viewPager2;
    private CircleIndicator3 circleIndicator3;
    private RecyclerView recyclerViewCategory;
    private RecyclerView recyclerViewProduct;
    private NestedScrollView nestedScrollView;
    private ProgressBar progressBarBanner, progressBarCategories, progressBarProducts;
    private SwipeRefreshLayout swipeRefreshLayout;

    // Adapter
    private BannerAdapter bannerAdapter;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

    // List data
    private List<Banner> mListBanner;
    private List<Category> mListCategories;
    private List<Product> mListProducts = new ArrayList<>();

    // Page ????? call api ph??n trang product
    private int page = 1;
    private int totalPage;

    // Khai b??o handler v?? 1 lu???ng ch???y ????? auto slide c??i banner
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(mListBanner !=null){
                // N???u tr?????t ?????n ph???n t??? cu???i r???i th?? quay l???i c??i ?????u ti??n
                if(viewPager2.getCurrentItem() == mListBanner.size()-1){
                    viewPager2.setCurrentItem(0);
                }else // C??n kh??ng th?? c??? tr?????t ?????n trang ti???p theo
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // init view
        viewPager2 = view.findViewById(R.id.view_pager_2_banner);
        circleIndicator3 = view.findViewById(R.id.circle_indicator_3_banner);
        recyclerViewCategory = view.findViewById(R.id.recycler_view_categories);
        recyclerViewProduct = view.findViewById(R.id.recycler_view_products);
        nestedScrollView = view.findViewById(R.id.nested_scroll_view_new_products);
        progressBarBanner = view.findViewById(R.id.progress_bar_banner);
        progressBarCategories = view.findViewById(R.id.progress_bar_categories);
        progressBarProducts = view.findViewById(R.id.progress_bar_new_product);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);

        // G???n adapter v??o viewpager, g???n viewpager v??o circle indicator
        bannerAdapter = new BannerAdapter(mListBanner,getActivity());
        viewPager2.setAdapter(bannerAdapter);
        circleIndicator3.setViewPager(viewPager2);

        // X??? l?? auto slide banner
        // L??c m???i start fragment l??n, n?? s??? ch???y v??o s??? ki???n onPageSelected t???c l?? page 1 ???????c ch???n
        // => start lu???ng trong mRunnable
        // sau 2.5 gi??y, n?? l???y ch???y ??o???n code trong mRunnable, m?? ??o???n code trong mRunnable l???i chuy???n trang
        // => trigger s??? ki???n onPageSelected do trang ti???p theo ???????c ch???n
        // => ??o???n code trong mRunnable l???i ???????c ch???y sau 2.5 s
        // => C??? th??? t???o th??nh v??ng l???p
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable,2500);
            }
        });

        categoryAdapter = new CategoryAdapter(mListCategories, getActivity());
        recyclerViewCategory.setAdapter(categoryAdapter);
        recyclerViewCategory.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));


        productAdapter = new ProductAdapter(mListProducts, getActivity());
        recyclerViewProduct.setAdapter(productAdapter);
        recyclerViewProduct.setLayoutManager(new GridLayoutManager(getActivity(),2));
        ViewCompat.setNestedScrollingEnabled(recyclerViewProduct, false);
        // T?? n???a khi call api th?? s???a l???i th??nh t??ng s??? page l??n v?? g???i ti???p api
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if(page>totalPage) {
                        progressBarProducts.setVisibility(View.GONE);
                    }else{
                        progressBarProducts.setVisibility(View.VISIBLE);
                        callApiProducts(page);
                    }
                }
            }
        });


        callApiBanner();
        callApiCategory();
        callApiProducts(page);


        // Xu ly su kien khi click vao 1 item category:
        categoryAdapter.setItemClickedListener(new CategoryAdapter.ItemClickedListener() {
            @Override
            public void onItemClicked(Category category) {
                Intent intent = new Intent(getActivity(), ProductsByCategoryActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);
            }
        });

        // X??? l?? s??? ki???n khi click v??o 1 item product
        productAdapter.setItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int productId) {
                Intent intent = new Intent(getActivity(),DetailProductActivity.class);
                intent.putExtra("productId",productId);
                startActivity(intent);
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                mListProducts.clear();
                productAdapter.setmListProduct(mListProducts);
                progressBarProducts.setVisibility(View.VISIBLE);
                callApiBanner();
                callApiCategory();
                callApiProducts(page);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    private void callApiCategory() {
        APIUtils.getApiServiceInterface().getListCategories()
                .enqueue(new Callback<List<Category>>() {
                    @Override
                    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                        progressBarCategories.setVisibility(View.GONE);
                        mListCategories = response.body();
                        categoryAdapter.setmListCategory(mListCategories);
                    }

                    @Override
                    public void onFailure(Call<List<Category>> call, Throwable t) {

                    }
                });
    }

    private void callApiBanner(){
        APIServiceInterface apiServiceInterface = APIUtils.getApiServiceInterface();
        Call<List<Banner>> call = apiServiceInterface.getListBanners();
        call.enqueue(new Callback<List<Banner>>() {
            @Override
            public void onResponse(Call<List<Banner>> call, Response<List<Banner>> response) {
                Log.d("RESPONSE", response.body().toString());
                progressBarBanner.setVisibility(View.GONE);
                mListBanner = response.body();
                bannerAdapter.setmListBanner(mListBanner);
                viewPager2.setAdapter(bannerAdapter);
                circleIndicator3.setViewPager(viewPager2);
            }

            @Override
            public void onFailure(Call<List<Banner>> call, Throwable t) {

            }
        });
    }


    private void callApiProducts(int page){
        APIUtils.getApiServiceInterface().getListProducts(page)
                .enqueue(new Callback<ProductPagination>() {
                    @Override
                    public void onResponse(Call<ProductPagination> call, Response<ProductPagination> response) {
                        if(response.isSuccessful() && response.body()!=null) {
                            progressBarProducts.setVisibility(View.GONE);
                            ProductPagination productPagination = response.body();
                            totalPage = productPagination.getTotal_pages();

                            mListProducts.addAll(productPagination.getResults());
                            productAdapter.setmListProduct(mListProducts);

                            HomeFragment.this.page++;
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductPagination> call, Throwable t) {

                    }
                });

    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable,3);

    }


}
