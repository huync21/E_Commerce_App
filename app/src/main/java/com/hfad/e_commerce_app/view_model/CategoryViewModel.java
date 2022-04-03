package com.hfad.e_commerce_app.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hfad.e_commerce_app.models.Category;

import java.util.List;

public class CategoryViewModel extends ViewModel {
    private MutableLiveData<List<Category>> listCategory;
    public CategoryViewModel() {
        listCategory = new MutableLiveData<>();
    }
}
