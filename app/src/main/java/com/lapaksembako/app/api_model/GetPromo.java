package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.Promo;

import java.util.ArrayList;
import java.util.List;

public class GetPromo {
    @SerializedName("status")
    String status;

    public List<Promo> getPromos() {
        return promos;
    }

    public void setPromos(ArrayList<Promo> promos) {
        this.promos = promos;
    }

    @SerializedName("data")
    List<Promo> promos;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
