package com.hfad.e_commerce_app.retrofit;

import com.hfad.e_commerce_app.models.Banner;
import com.hfad.e_commerce_app.models.Category;
import com.hfad.e_commerce_app.models.Product;
import com.hfad.e_commerce_app.models.ProductPagination;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIServiceInterface {
    @GET("banners")
    Call<List<Banner>> getListBanners();

    @GET("categories")
    Call<List<Category>> getListCategories();

    @GET("products")
    Call<ProductPagination> getListProducts(@Query("page") int page);

    @GET("products/search")
    Call<List<ProductPagination>> searchListProducts(@Query("keyword") String keyword);
}
