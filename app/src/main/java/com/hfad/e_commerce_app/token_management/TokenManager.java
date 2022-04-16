package com.hfad.e_commerce_app.token_management;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;

public class TokenManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int Mode = 0;
    public static final String REFNAME = "JWT_SHARED_PREFERENCE";
    public static final String USER_EMAIL = "email";
    public static final String USER_FIRST_NAME = "firstName";
    public static final String USER_LAST_NAME = "lastName";
    public static final String USER_PHONE = "phone";
    public static final String ACCESS_TOKEN = "accessTOKEN";
    public static final String REFRESH_TOKEN = "refreshTOKEN";
    public static final String IMAGE_URL = "imgurl";
    private Context context;

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public TokenManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(REFNAME,Mode);
        editor = sharedPreferences.edit();
    }

    public void saveUserInfo(String email, String firstName, String lastName, String phone, String accessToken, String refreshToken, String image){
        editor.putString(USER_EMAIL,email);
        editor.putString(USER_FIRST_NAME,firstName);
        editor.putString(USER_LAST_NAME,lastName);
        editor.putString(USER_PHONE,phone);
        editor.putString(ACCESS_TOKEN,accessToken);
        editor.putString(REFRESH_TOKEN,refreshToken);
        editor.putString(IMAGE_URL, image);
        editor.apply();
    }

    public void removeTokenAndUserInfo(){
        editor.remove(USER_EMAIL);
        editor.remove(USER_FIRST_NAME);
        editor.remove(USER_LAST_NAME);
        editor.remove(USER_PHONE);
        editor.remove(ACCESS_TOKEN);
        editor.remove(REFRESH_TOKEN);
        editor.remove(IMAGE_URL);
        editor.apply();
    }

    public String getAccessToken(){
        return sharedPreferences.getString(ACCESS_TOKEN,null);
    }

    public String getUserEmail(){
        return sharedPreferences.getString(USER_EMAIL,null);
    }

    public String getUserFirstName(){
        return sharedPreferences.getString(USER_FIRST_NAME,null);

    }

    public String getUserLastName(){
        return sharedPreferences.getString(USER_LAST_NAME,null);
    }

    public String getUserPhone(){
        return sharedPreferences.getString(USER_PHONE,null);
    }

    public String getImageUrl(){
        return sharedPreferences.getString(IMAGE_URL,null);
    }

}
