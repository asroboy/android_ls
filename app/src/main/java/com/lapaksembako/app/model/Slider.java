package com.lapaksembako.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Slider implements Parcelable {

    @SerializedName("id_slider")
    int id;

    @SerializedName("title")
    String title;

    @SerializedName("image")
    String image;

    @SerializedName("url")
    String url;

    @SerializedName("sequence_number")
    int sequenceNumber;

    protected Slider(Parcel in) {
        id = in.readInt();
        title = in.readString();
        image = in.readString();
        url = in.readString();
        sequenceNumber = in.readInt();
        type = in.readString();
    }

    public static final Creator<Slider> CREATOR = new Creator<Slider>() {
        @Override
        public Slider createFromParcel(Parcel in) {
            return new Slider(in);
        }

        @Override
        public Slider[] newArray(int size) {
            return new Slider[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("type")
    String type;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(image);
        parcel.writeString(url);
        parcel.writeInt(sequenceNumber);
        parcel.writeString(type);
    }
}
