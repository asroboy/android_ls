package com.lapaksembako.app.model;

import com.google.gson.annotations.SerializedName;

public class Province {
    @SerializedName("province_id")
    int id;
    @SerializedName("province")
    String name;

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
