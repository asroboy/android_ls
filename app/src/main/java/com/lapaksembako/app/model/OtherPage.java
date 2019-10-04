package com.lapaksembako.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class OtherPage implements Parcelable {


    @SerializedName("id_page")
    int id;
    @SerializedName("title")
    String title;
    @SerializedName("content")
    String content;
    @SerializedName("slug")
    String slug;
    @SerializedName("image")
    String image;

    protected OtherPage(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        slug = in.readString();
        image = in.readString();
    }

    public static final Creator<OtherPage> CREATOR = new Creator<OtherPage>() {
        @Override
        public OtherPage createFromParcel(Parcel in) {
            return new OtherPage(in);
        }

        @Override
        public OtherPage[] newArray(int size) {
            return new OtherPage[size];
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(slug);
        parcel.writeString(image);
    }
}
