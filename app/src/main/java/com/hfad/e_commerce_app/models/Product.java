package com.hfad.e_commerce_app.models;

public class Product {
    private String name;
    private String image;
    private int price;
    private String description;
    private int imageTest;

    public Product(String name, int price, int imageTest) {
        this.name = name;
        this.price = price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Product() {
    }

    public Product(String name, String image, int price, String description) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.description = description;
    }
}
