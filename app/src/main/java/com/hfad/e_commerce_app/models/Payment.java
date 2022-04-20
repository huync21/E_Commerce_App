package com.hfad.e_commerce_app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Payment implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("image")
    @Expose
    private String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Payment(int id, String paymentMethod, String image) {
        this.id = id;
        this.paymentMethod = paymentMethod;
        this.image = image;
    }

    public Payment() {
    }
}
