package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.Address;
import com.lapaksembako.app.model.Balance;

import java.util.ArrayList;
import java.util.List;

public class GetBalance {
    @SerializedName("status")
    String status;

    public List<Balance> getBalances() {
        return balances;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

    @SerializedName("data")
    List<Balance> balances;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
