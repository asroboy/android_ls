package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;

public class GetUserDaftar {
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

    @SerializedName("status")
    String status;

    @SerializedName("message")
    String message;
}
