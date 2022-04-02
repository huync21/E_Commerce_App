package com.hfad.e_commerce_app.views;

import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class HomeFragment extends Fragment {
    // View components
    private ViewPager2 viewPager2;
    private CircleIndicator3 circleIndicator3;
    private RecyclerView recyclerViewCategory;
    private RecyclerView recyclerViewProduct;
    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;

    // Adapter
    private BannerAdapter bannerAdapter;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

    // List data
    private List<Banner> mListBanner;
    private List<Category> mListCategories;
    private List<Product> mListProducts;

    // Khai báo handler và 1 luồng chạy để auto slide cái banner
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            // Nếu trượt đến phần tử cuối rồi thì quay lại cái đầu tiên
            if(viewPager2.getCurrentItem() == mListBanner.size()-1){
                viewPager2.setCurrentItem(0);
            }else // Còn không thì cứ trượt đến trang tiếp theo
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
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
        progressBar = view.findViewById(R.id.progress_bar_new_product);

        // Tí lấy data từ repository call từ api
        mListBanner = initializeTestDataBanners();

        // Gắn adapter vào viewpager, gắn viewpager vào circle indicator
        bannerAdapter = new BannerAdapter(mListBanner);
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

        // Gắn adapter vào recycler view phần Category:
        mListCategories = initializeTestDataCategory();
        categoryAdapter = new CategoryAdapter(mListCategories);
        recyclerViewCategory.setAdapter(categoryAdapter);
        recyclerViewCategory.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));

        // Gắn adapter vào recycler view phần Product:
        mListProducts = initializeTestDataProducts();
        productAdapter = new ProductAdapter(mListProducts);
        recyclerViewProduct.setAdapter(productAdapter);
        recyclerViewProduct.setLayoutManager(new GridLayoutManager(getActivity(),2));

        // Tí nữa khi call api thì sửa lại thành tăng số page lên và gọi tiếp api
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


    }

    private List<Banner> initializeTestDataBanners(){
        List<Banner> bannerList = new ArrayList<>();
        bannerList.add(new Banner(R.drawable.banner_e_commerce1));
        bannerList.add(new Banner(R.drawable.banner));
        bannerList.add(new Banner(R.drawable.shopping_trendy_banner));
        return bannerList;
    }

    private List<Category> initializeTestDataCategory(){
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Shoes",R.drawable.shoes));
        categories.add(new Category("TVs",R.drawable.tv));
        categories.add(new Category("Clothes",R.drawable.clothes));
        categories.add(new Category("Books",R.drawable.books));
        categories.add(new Category("Computers",R.drawable.computer_and_laptops));
        categories.add(new Category("Phones",R.drawable.phones));
        categories.add(new Category("Vehicles",R.drawable.vehicles));
        return categories;
    }

    private List<Product> initializeTestDataProducts(){
        List<Product> listProducts = new ArrayList<>();
        listProducts.add(new Product("Nike Shoe",1000,R.drawable.nike));
        listProducts.add(new Product("Cool Hoodie",1500,R.drawable.hoodie));
        listProducts.add(new Product("Yellow Hoodie",1000,R.drawable.images));
        listProducts.add(new Product("Nike Shoe",1000,R.drawable.nike));
        listProducts.add(new Product("Cool Hoodie",1500,R.drawable.hoodie));
        listProducts.add(new Product("Yellow Hoodie",1000,R.drawable.images));
        return listProducts;
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
