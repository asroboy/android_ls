package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.DeliveryPrice;

import java.util.List;

public class GetDelivery {
    @SerializedName("status")
    String status;

    @SerializedName("message")
    String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DeliveryPrice> getDeliveryPrices() {
        return deliveryPrices;
    }

    public void setDeliveryPrices(List<DeliveryPrice> deliveryPrices) {
        this.deliveryPrices = deliveryPrices;
    }

    @SerializedName("data")
    List<DeliveryPrice> deliveryPrices;
}
