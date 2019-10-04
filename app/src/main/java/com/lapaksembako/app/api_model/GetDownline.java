package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.Faq;

import java.util.List;

public class GetDownline {
    @SerializedName("status")
    String status;

    @SerializedName("data")
    int jumlahDownline;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getJumlahDownline() {
        return jumlahDownline;
    }

    public void setJumlahDownline(int jumlahDownline) {
        this.jumlahDownline = jumlahDownline;
    }
}
