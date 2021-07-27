package com.example.e_commerce.Pojo;

import java.io.Serializable;

public class ItemClass implements Serializable {
    private String image;
    private int quantity;
    private int price;
    private String name;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemClass(String image, int quantity, int price, String name) {
        this.image = image;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
    }

    public ItemClass() {
    }
}
