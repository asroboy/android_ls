package com.lapaksembako.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Promo {
    @SerializedName("id_promo")
    int id;

    @SerializedName("promo_name")
    String nama;

    @SerializedName("valid_start")
    Date mulai;

    @SerializedName("valid_end")
    Date sampai;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Date getMulai() {
        return mulai;
    }

    public void setMulai(Date mulai) {
        this.mulai = mulai;
    }

    public Date getSampai() {
        return sampai;
    }

    public void setSampai(Date sampai) {
        this.sampai = sampai;
    }
}
