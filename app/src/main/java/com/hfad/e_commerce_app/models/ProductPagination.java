package com.hfad.e_commerce_app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductPagination implements Serializable {
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("total_pages")
    @Expose
    private int total_pages;
    @SerializedName("results")
    @Expose
    private List<Product> results;

    public ProductPagination() {
    }

    public ProductPagination(int count, int total_pages, List<Product> results) {
        this.count = count;
        this.total_pages = total_pages;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<Product> getResults() {
        return results;
    }

    public void setResults(List<Product> results) {
        this.results = results;
    }
}
