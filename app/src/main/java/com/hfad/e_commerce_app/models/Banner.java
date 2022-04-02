package com.hfad.e_commerce_app.models;

public class Banner {
    private String image;
    private String name;
    private int imageIdTest;

    public int getImageIdTest() {
        return imageIdTest;
    }

    public void setImageIdTest(int imageIdTest) {
        this.imageIdTest = imageIdTest;
    }

    public Banner(String image, String name, int imageIdTest) {
        this.image = image;
        this.name = name;
        this.imageIdTest = imageIdTest;
    }

    public Banner(int imageIdTest) {
        this.imageIdTest = imageIdTest;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Banner() {
    }

    public Banner(String image, String name) {
        this.image = image;
        this.name = name;
    }
}
