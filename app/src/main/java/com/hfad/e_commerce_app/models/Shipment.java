package com.hfad.e_commerce_app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Shipment implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("service_provider")
    @Expose
    private String serviceProvider;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("price")
    @Expose
    private int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Shipment() {
    }

    public Shipment(int id, String serviceProvider, String image, int price) {
        this.id = id;
        this.serviceProvider = serviceProvider;
        this.image = image;
        this.price = price;
    }
}
