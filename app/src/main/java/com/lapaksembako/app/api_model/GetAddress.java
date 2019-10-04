package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.Address;
import com.lapaksembako.app.model.Kategori;

import java.util.ArrayList;
import java.util.List;

public class GetAddress {
    @SerializedName("status")
    String status;

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<Address> address) {
        this.address = address;
    }

    @SerializedName("data")
    List<Address> address;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
