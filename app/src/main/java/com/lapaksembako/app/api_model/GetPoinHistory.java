package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.Balance;
import com.lapaksembako.app.model.Poin;

import java.util.List;

public class GetPoinHistory {
    @SerializedName("status")
    String status;

    public List<Poin> getPoins() {
        return poins;
    }

    public void setPoins(List<Poin> poins) {
        this.poins = poins;
    }

    @SerializedName("data")
    List<Poin> poins;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
