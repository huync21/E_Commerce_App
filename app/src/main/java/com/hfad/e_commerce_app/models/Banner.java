package com.hfad.e_commerce_app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Banner {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("banner_name")
    @Expose
    private String bannerName;
    @SerializedName("image")
    @Expose
    private String image;

    public Banner() {
    }

    public Banner(int id, String bannerName, String image) {
        this.id = id;
        this.bannerName = bannerName;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
