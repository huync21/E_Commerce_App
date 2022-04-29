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
    @SerializedName("shipping_price")
    @Expose
    private int shippingPrice;
    @SerializedName("order_total")
    @Expose
    private int orderTotal;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("shipment")
    @Expose
    private Shipment shipment;
    @SerializedName("payment")
    @Expose
    private Payment payment;

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

    public Order(int id, String phone, String shippingAddress, int totalPrice, int shippingPrice, int orderTotal, String status, String createdAt, Shipment shipment, Payment payment) {
        this.id = id;
        this.phone = phone;
        this.shippingAddress = shippingAddress;
        this.totalPrice = totalPrice;
        this.shippingPrice = shippingPrice;
        this.orderTotal = orderTotal;
        this.status = status;
        this.createdAt = createdAt;
        this.shipment = shipment;
        this.payment = payment;
    }

    public Order() {
    }

    public int getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(int shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
