package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.User;

public class GetUserLogin {

    @SerializedName("status")
    String status;

    @SerializedName("data")
    User dataUser;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getDataUser() {
        return dataUser;
    }

    public void setDataUser(User dataUser) {
        this.dataUser = dataUser;
    }


}
