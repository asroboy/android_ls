package com.lapaksembako.app.model;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Cart  {
    @SerializedName("id_cart")
    int id;

    @SerializedName("id_member")
    int userId;

    @SerializedName("wholesale_status")
    boolean isGrosir;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isGrosir() {
        return isGrosir;
    }

    public void setGrosir(boolean grosir) {
        isGrosir = grosir;
    }
}
