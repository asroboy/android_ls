package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.AkunBank;
import com.lapaksembako.app.model.City;

import java.util.ArrayList;
import java.util.List;

public class GetAkunBank {
    @SerializedName("status")
    String status;

    public List<AkunBank> getAkunBanks() {
        return akunBanks;
    }

    public void setAkunBanks(ArrayList<AkunBank> akunBanks) {
        this.akunBanks = akunBanks;
    }

    @SerializedName("data")
    List<AkunBank> akunBanks;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
