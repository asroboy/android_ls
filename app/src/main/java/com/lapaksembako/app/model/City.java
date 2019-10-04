package com.lapaksembako.app.model;

import com.google.gson.annotations.SerializedName;

public class City {
    @SerializedName("city_id")
    int id;
    @SerializedName("city_name")
    String name;

    int provinceId;

    @SerializedName("province_id")
    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
