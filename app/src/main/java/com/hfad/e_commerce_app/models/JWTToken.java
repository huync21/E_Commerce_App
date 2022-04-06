package com.hfad.e_commerce_app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JWTToken {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("username")
    @Expose
    private String userName;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;
    @SerializedName("access_token")
    @Expose
    private String accessToken;

    public JWTToken(String email, String userName, String firstName, String lastName, String refreshToken, String accessToken) {
        this.email = email;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public JWTToken() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "JWTToken{" +
                "email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
