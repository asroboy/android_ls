package com.lapaksembako.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Kategori implements Parcelable {
    @SerializedName("category_name")
    String nama;
    @SerializedName("id_product_category")
    int id;

    protected Kategori(Parcel in) {
        nama = in.readString();
        id = in.readInt();
        imageResource = in.readInt();
        icon = in.readString();
    }

    public static final Creator<Kategori> CREATOR = new Creator<Kategori>() {
        @Override
        public Kategori createFromParcel(Parcel in) {
            return new Kategori(in);
        }

        @Override
        public Kategori[] newArray(int size) {
            return new Kategori[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int imageResource = 0;

    @SerializedName("icon")
    String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Kategori(String nama, int resImage) {
        this.nama = nama;
        this.imageResource = resImage;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nama);
        parcel.writeInt(id);
        parcel.writeInt(imageResource);
        parcel.writeString(icon);
    }
}
