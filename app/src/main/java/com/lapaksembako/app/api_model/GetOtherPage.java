package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.Balance;
import com.lapaksembako.app.model.OtherPage;

import java.util.List;

public class GetOtherPage {
    @SerializedName("status")
    String status;

    public OtherPage getOtherPage() {
        return otherPage;
    }

    public void setOtherPage(OtherPage otherPage) {
        this.otherPage = otherPage;
    }

    @SerializedName("data")
    OtherPage otherPage;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
