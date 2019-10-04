package com.lapaksembako.app.model;

import com.google.gson.annotations.SerializedName;

public class DeliveryPrice {

    @SerializedName("price")
    int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
