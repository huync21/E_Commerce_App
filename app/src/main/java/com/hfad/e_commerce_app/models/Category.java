package com.hfad.e_commerce_app.models;

public class Category {
    private String name;
    private String image;
    private int imageTest;

    public Category(String name, int imageTest) {
        this.name = name;
        this.imageTest = imageTest;
    }

    public int getImageTest() {
        return imageTest;
    }

    public void setImageTest(int imageTest) {
        this.imageTest = imageTest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public Category() {
    }
}
