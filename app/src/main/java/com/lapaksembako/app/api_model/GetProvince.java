package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.Address;
import com.lapaksembako.app.model.Province;

import java.util.ArrayList;
import java.util.List;

public class GetProvince {
    @SerializedName("status")
    String status;

    public List<Province> getProvinces() {
        return provinces;
    }

    public void setProvinces(ArrayList<Province> address) {
        this.provinces = provinces;
    }

    @SerializedName("data")
    List<Province> provinces;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
