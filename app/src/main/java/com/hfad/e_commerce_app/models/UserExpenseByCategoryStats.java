package com.hfad.e_commerce_app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserExpenseByCategoryStats implements Serializable {
    @SerializedName("total_of_user")
    @Expose
    private int totalOfUser;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("total")
    @Expose
    private int total;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public UserExpenseByCategoryStats(String category, int total) {
        this.category = category;
        this.total = total;
    }

    public UserExpenseByCategoryStats() {
    }

    public int getTotalOfUser() {
        return totalOfUser;
    }

    public void setTotalOfUser(int totalOfUser) {
        this.totalOfUser = totalOfUser;
    }
}
