package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.Cart;
import com.lapaksembako.app.model.Item;

import java.util.ArrayList;
import java.util.List;

public class GetCart {
    @SerializedName("status")
    String status;

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(ArrayList<Cart> items) {
        this.carts = carts;
    }

    @SerializedName("data")
    List<Cart> carts;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
