package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.Item;

import java.util.ArrayList;
import java.util.List;

public class GetItems {
    @SerializedName("status")
    String status;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    @SerializedName("data")
    List<Item> items;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
