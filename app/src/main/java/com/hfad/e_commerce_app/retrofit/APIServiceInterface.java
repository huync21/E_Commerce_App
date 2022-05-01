package com.hfad.e_commerce_app.retrofit;

import com.hfad.e_commerce_app.models.Banner;
import com.hfad.e_commerce_app.models.CartItem;
import com.hfad.e_commerce_app.models.Category;
import com.hfad.e_commerce_app.models.JWTToken;
import com.hfad.e_commerce_app.models.Order;
import com.hfad.e_commerce_app.models.OrderProduct;
import com.hfad.e_commerce_app.models.Payment;
import com.hfad.e_commerce_app.models.Product;
import com.hfad.e_commerce_app.models.ProductPagination;
import com.hfad.e_commerce_app.models.Rating;
import com.hfad.e_commerce_app.models.Shipment;
import com.hfad.e_commerce_app.models.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIServiceInterface {
    @FormUrlEncoded
    @POST("accounts/login")
    Call<JWTToken> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("accounts/register")
    Call<User> register(@Field("email") String email,
                        @Field("password") String password,
                        @Field("username") String userName,
                        @Field("first_name") String firstName,
                        @Field("last_name") String lastName);

    @GET("accounts/")
    Call<User> getUserInfo(@Header("Authorization") String authHeader);

    @Multipart
    @PUT("accounts/")
    Call<ResponseBody> changeUserInfo(@Header("Authorization") String authHeader,
                                      @Part("username") RequestBody userName,
                                      @Part("first_name") RequestBody firstName,
                                      @Part("last_name") RequestBody lastName,
                                      @Part("phone_number") RequestBody phone,
                                      @Part MultipartBody.Part imageFile);

    @GET("banners")
    Call<List<Banner>> getListBanners();

    @GET("categories")
    Call<List<Category>> getListCategories();

    @GET("products")
    Call<ProductPagination> getListProducts(@Query("page") int page);


    @GET("products/by-category")
    Call<ProductPagination> getListProductsByCategory(@Query("category") String category, @Query("page") int page);

    @GET("products/search")
    Call<ProductPagination> searchListProducts(@Query("keyword") String keyword, @Query("page") int page);

    @GET("products/{productId}")
    Call<Product> getProductDetail(@Path("productId") int productId);

    @GET("ratings/average-star/")
    Call<ResponseBody> getAverageStarOfProduct(@Query("product_id") int productId);

    @GET("ratings/")
    Call<List<Rating>> getAllRatingsOfProduct(@Query("product_id") int productId);

    @FormUrlEncoded
    @POST("ratings/")
    Call<ResponseBody> createRating(@Header("Authorization") String authHeader, @Field("star_num") int starNum, @Field("comment") String comment, @Query("product_id") int productId);

    @GET("carts")
    Call<List<CartItem>> getAllCartItems(@Header("Authorization") String authHeader);

    @FormUrlEncoded
    @POST("carts/")
    Call<ResponseBody> createOrUpdateCartItem(@Header("Authorization") String authHeader,
                                              @Field("quantity") int quantity,
                                              @Field("product_id") int productId);

    @FormUrlEncoded
    @PUT("carts/{cart_id}/")
    Call<ResponseBody> updateCartItem(@Header("Authorization") String authHeader, @Field("quantity") int quantity,
                                      @Path("cart_id") int cartId);

    @DELETE("carts/{cartId}/")
    Call<ResponseBody> deleteCartItem(@Header("Authorization") String authHeader, @Path("cartId") int cartId);

    @GET("orders/payment-methods")
    Call<List<Payment>> getAllPaymentMethods();

//    @GET("orders/payment-method")
//    Call<Payment> getPayment(@Header("Authorization") String authHeader, @Query("order_id") int orderId);

    @GET("orders/shipment-methods")
    Call<List<Shipment>> getAllShipmentMethods();

//    @GET("orders/shipment-method")
//    Call<Shipment> getShipment(@Header("Authorization") String authHeader, @Query("order_id") int orderId);

    @GET("orders/")
    Call<List<Order>> getAllOrders(@Header("Authorization") String authHeader);

    @GET("orders/detail/")
    Call<List<OrderProduct>> getOrderDetail(@Header("Authorization") String authHeader, @Query("order_id") int orderId);

    @POST("orders/")
    @FormUrlEncoded
    Call<ResponseBody> saveTransactionInfo(@Header("Authorization") String authHeader,
                                           @Field("phone") String phone,
                                           @Field("shipping_address") String shippingAddress,
                                           @Field("list_cart_item_id") List<Integer> listCartItemId,
                                           @Field("payment_id") int paymentId,
                                           @Field("shipment_id") int shipmentId,
                                           @Field("total_price") int totalPrice);
}
