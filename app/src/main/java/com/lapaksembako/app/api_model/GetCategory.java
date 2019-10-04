package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.Faq;
import com.lapaksembako.app.model.Kategori;

import java.util.ArrayList;
import java.util.List;

public class GetCategory {
    @SerializedName("status")
    String status;

    public List<Kategori> getKategoris() {
        return kategoris;
    }

    public void setKategoris(ArrayList<Kategori> kategoris) {
        this.kategoris = kategoris;
    }

    @SerializedName("data")
    List<Kategori> kategoris;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
