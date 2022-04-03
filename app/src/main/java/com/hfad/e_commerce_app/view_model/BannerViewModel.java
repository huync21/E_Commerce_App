package com.hfad.e_commerce_app.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hfad.e_commerce_app.models.Banner;

import java.util.List;

public class BannerViewModel extends ViewModel {

    private MutableLiveData<List<Banner>> listBanner;
    public BannerViewModel() {
        listBanner = new MutableLiveData<>();
    }

}
