package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.Cart;
import com.lapaksembako.app.model.Slider;

import java.util.ArrayList;
import java.util.List;

public class GetSliders {
    @SerializedName("status")
    String status;

    public List<Slider> getSliders() {
        return sliders;
    }

    public void setSliders(List<Slider> sliders) {
        this.sliders = sliders;
    }

    @SerializedName("data")
    List<Slider> sliders;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
