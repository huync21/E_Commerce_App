package com.hfad.e_commerce_app.retrofit;

import com.hfad.e_commerce_app.models.Banner;
import com.hfad.e_commerce_app.models.Category;
import com.hfad.e_commerce_app.models.JWTToken;
import com.hfad.e_commerce_app.models.Product;
import com.hfad.e_commerce_app.models.ProductPagination;
import com.hfad.e_commerce_app.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIServiceInterface {
    @FormUrlEncoded
    @POST("login/")
    Call<JWTToken> login(@Field("email") String email,@Field("password") String password);

    @FormUrlEncoded
    @POST("register/")
    Call<User> register(@Field("email") String email, @Field("password") String password, @Field("username") String userName
    , @Field("first_name") String firstName, @Field("last_name") String lastName);

    @GET("banners")
    Call<List<Banner>> getListBanners();

    @GET("categories")
    Call<List<Category>> getListCategories();

    @GET("products")
    Call<ProductPagination> getListProducts(@Query("page") int page);

    @GET("products/search")
    Call<ProductPagination> searchListProducts(@Query("keyword") String keyword,@Query("page") int page);

    @GET("products/{productId}")
    Call<Product> getProductDetail(@Path("productId") int productId);
}
