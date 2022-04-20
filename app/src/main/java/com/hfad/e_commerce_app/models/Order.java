package com.hfad.e_commerce_app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Order implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("shipping_address")
    @Expose
    private String shippingAddress;
    @SerializedName("total_price")
    @Expose
    private int totalPrice;
    @SerializedName("order_total")
    @Expose
    private int orderTotal;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(int orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Order(int id, String phone, String shippingAddress, int totalPrice, int orderTotal, String status, String createdAt) {
        this.id = id;
        this.phone = phone;
        this.shippingAddress = shippingAddress;
        this.totalPrice = totalPrice;
        this.orderTotal = orderTotal;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Order() {
    }
}
