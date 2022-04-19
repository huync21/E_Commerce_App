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
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import java.io.Serializable;
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

    // Adapter
    private BannerAdapter bannerAdapter;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

    // List data
    private List<Banner> mListBanner;
    private List<Category> mListCategories;
    private List<Product> mListProducts = new ArrayList<>();

    // Page để call api phân trang product
    private int page = 1;
    private int totalPage;

    // Khai báo handler và 1 luồng chạy để auto slide cái banner
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(mListBanner !=null){
                // Nếu trượt đến phần tử cuối rồi thì quay lại cái đầu tiên
                if(viewPager2.getCurrentItem() == mListBanner.size()-1){
                    viewPager2.setCurrentItem(0);
                }else // Còn không thì cứ trượt đến trang tiếp theo
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


        // Gắn adapter vào viewpager, gắn viewpager vào circle indicator
        bannerAdapter = new BannerAdapter(mListBanner,getActivity());
        viewPager2.setAdapter(bannerAdapter);
        circleIndicator3.setViewPager(viewPager2);

        // Xử lý auto slide banner
        // Lúc mới start fragment lên, nó sẽ chạy vào sự kiện onPageSelected tức là page 1 được chọn
        // => start luồng trong mRunnable
        // sau 2.5 giây, nó lạy chạy đoạn code trong mRunnable, mà đoạn code trong mRunnable lại chuyển trang
        // => trigger sự kiện onPageSelected do trang tiếp theo được chọn
        // => đoạn code trong mRunnable lại được chạy sau 2.5 s
        // => Cứ thế tạo thành vòng lặp
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

        // Tí nữa khi call api thì sửa lại thành tăng số page lên và gọi tiếp api
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    if(page>totalPage) {
                        progressBarProducts.setVisibility(View.GONE);
                    }else{
                        progressBarProducts.setVisibility(View.VISIBLE);
                        callApiProducts(page);
                    }
                }
            }
        });

        if(savedInstanceState==null) {
            callApiBanner();
            callApiCategory();
            callApiProducts(page);
        } else{
            mListBanner = (List<Banner>) savedInstanceState.getSerializable("listBanners");
            mListCategories = (List<Category>) savedInstanceState.getSerializable("listCategories");
            mListProducts = (List<Product>) savedInstanceState.getSerializable("listProducts");
            page = savedInstanceState.getInt("page");
            totalPage = savedInstanceState.getInt("totalPage");
            bannerAdapter.setmListBanner(mListBanner);
            categoryAdapter.setmListCategory(mListCategories);
            productAdapter.setmListProduct(mListProducts);
        }

        // Xử lý sự kiện khi click vào 1 item product
        productAdapter.setItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int productId) {
                Intent intent = new Intent(getActivity(),DetailProductActivity.class);
                intent.putExtra("productId",productId);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("listBanners", (Serializable) mListBanner);
        outState.putSerializable("listCategories",(Serializable) mListCategories);
        outState.putSerializable("listProducts",(Serializable) mListProducts);
        outState.putInt("page",page);
        outState.putInt("totalPage",totalPage);

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
