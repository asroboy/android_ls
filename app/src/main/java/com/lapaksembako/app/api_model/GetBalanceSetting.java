package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.Balance;
import com.lapaksembako.app.model.BalanceSetting;

import java.util.List;

public class GetBalanceSetting {
    @SerializedName("status")
    String status;

    public BalanceSetting getBalanceSetting() {
        return balanceSetting;
    }

    public void setBalanceSetting(BalanceSetting balanceSetting) {
        this.balanceSetting = balanceSetting;
    }

    @SerializedName("data")
    BalanceSetting balanceSetting;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
