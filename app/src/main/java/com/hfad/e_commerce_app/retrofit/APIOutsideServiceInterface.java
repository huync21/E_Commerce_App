package com.hfad.e_commerce_app.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIOutsideServiceInterface {
    @POST("https://api.stripe.com/v1/customers")
    Call<ResponseBody> createStripeCustomer(@Header("Authorization") String authHeader);

    @POST("https://api.stripe.com/v1/ephemeral_keys")
    @FormUrlEncoded
    Call<ResponseBody> getEphemeralKey(@Header("Authorization") String authHeader, @Header("Stripe-Version") String stripeVersion,@Field("customer") String customerId);

    @POST("https://api.stripe.com/v1/payment_intents")
    @FormUrlEncoded
    Call<ResponseBody> getClientSecret(@Header("Authorization") String authHeader, @Field("customer")String customerId, @Field("amount") String amount,
                                       @Field("currency") String currency, @Field("automatic_payment_methods[enabled]") String automaticPaymentMethod);
}
