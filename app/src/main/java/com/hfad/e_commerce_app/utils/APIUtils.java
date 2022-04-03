package com.hfad.e_commerce_app.utils;

import com.hfad.e_commerce_app.retrofit.APIServiceInterface;
import com.hfad.e_commerce_app.retrofit.RetrofitInstance;

import retrofit2.Retrofit;

public class APIUtils {
    public static APIServiceInterface getApiServiceInterface(){
        Retrofit retrofitInstance = RetrofitInstance.getRetrofitInstance();
        return retrofitInstance.create(APIServiceInterface.class);
    }
}
