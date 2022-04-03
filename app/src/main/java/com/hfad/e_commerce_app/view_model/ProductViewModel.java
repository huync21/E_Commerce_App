package com.hfad.e_commerce_app.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hfad.e_commerce_app.models.Product;

import java.util.List;

public class ProductViewModel extends ViewModel {
    private MutableLiveData<List<Product>> listProduct;
    public ProductViewModel() {
        listProduct = new MutableLiveData<>();
    }

}
