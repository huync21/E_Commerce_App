package com.hfad.e_commerce_app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderToCreate implements Serializable {
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("shipping_address")
    @Expose
    private String shippingAddress;
    @SerializedName("list_cart_item_ids")
    @Expose
    private ArrayList<Integer> listCartItemIds;
    @SerializedName("payment_id")
    @Expose
    private Integer paymentId;
    @SerializedName("shipment_id")
    @Expose
    private Integer shipmentId;
    @SerializedName("total_price")
    @Expose
    private Integer totalPrice;

    public OrderToCreate(String phone, String shippingAddress, ArrayList<Integer> listCartItemIds, Integer paymentId, Integer shipmentId, Integer totalPrice) {
        this.phone = phone;
        this.shippingAddress = shippingAddress;
        this.listCartItemIds = listCartItemIds;
        this.paymentId = paymentId;
        this.shipmentId = shipmentId;
        this.totalPrice = totalPrice;
    }

    public OrderToCreate() {
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

    public ArrayList<Integer> getListCartItemIds() {
        return listCartItemIds;
    }

    public void setListCartItemIds(ArrayList<Integer> listCartItemIds) {
        this.listCartItemIds = listCartItemIds;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Integer shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }
}

