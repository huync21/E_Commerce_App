package com.hfad.e_commerce_app.token_management;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int Mode = 0;
    public static final String REFNAME = "JWT_SHARED_PREFERENCE";
    public static final String USER_EMAIL = "email";
    public static final String USER_FIRST_NAME = "firstName";
    public static final String USER_LAST_NAME = "lastName";
    public static final String ACCESS_TOKEN = "accessTOKEN";
    public static final String REFRESH_TOKEN = "refreshTOKEN";
    private Context context;

    public TokenManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(REFNAME,Mode);
        editor = sharedPreferences.edit();
    }

    public void createSession(String email, String firstName, String lastName, String accessToken, String refreshToken){
        editor.putString(USER_EMAIL,email);
        editor.putString(USER_FIRST_NAME,firstName);
        editor.putString(USER_LAST_NAME,lastName);
        editor.putString(ACCESS_TOKEN,accessToken);
        editor.putString(REFRESH_TOKEN,refreshToken);
        editor.commit();
    }

    public void logOut(){
        editor.remove(USER_EMAIL);
        editor.remove(USER_FIRST_NAME);
        editor.remove(USER_LAST_NAME);
        editor.remove(ACCESS_TOKEN);
        editor.remove(REFRESH_TOKEN);
        editor.apply();
    }
}
