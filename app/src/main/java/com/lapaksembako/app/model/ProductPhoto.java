package com.lapaksembako.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ProductPhoto implements Parcelable {

    @SerializedName("id_photo")
    int idPhoto;

    protected ProductPhoto(Parcel in) {
        idPhoto = in.readInt();
        idProduct = in.readInt();
        photoFileName = in.readString();
        description = in.readString();
    }

    public static final Creator<ProductPhoto> CREATOR = new Creator<ProductPhoto>() {
        @Override
        public ProductPhoto createFromParcel(Parcel in) {
            return new ProductPhoto(in);
        }

        @Override
        public ProductPhoto[] newArray(int size) {
            return new ProductPhoto[size];
        }
    };

    public int getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(int idPhoto) {
        this.idPhoto = idPhoto;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getPhotoFileName() {
        return photoFileName;
    }

    public void setPhotoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @SerializedName("id_product")
    int idProduct;

    @SerializedName("photo_file_name")
    String photoFileName;

    @SerializedName("description")
    String description;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idPhoto);
        parcel.writeInt(idProduct);
        parcel.writeString(photoFileName);
        parcel.writeString(description);
    }
}
