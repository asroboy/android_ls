package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.Address;
import com.lapaksembako.app.model.ProductPhoto;

import java.util.ArrayList;
import java.util.List;

public class GetPhotos {
    @SerializedName("status")
    String status;

    public List<ProductPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ProductPhoto> photos) {
        this.photos = photos;
    }

    @SerializedName("data")
    List<ProductPhoto> photos;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
