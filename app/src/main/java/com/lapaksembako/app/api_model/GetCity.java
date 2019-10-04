package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.City;
import com.lapaksembako.app.model.Province;

import java.util.ArrayList;
import java.util.List;

public class GetCity {
    @SerializedName("status")
    String status;

    public List<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

    @SerializedName("data")
    List<City> cities;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
