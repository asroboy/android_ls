package com.lapaksembako.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Poin implements Parcelable {
    @SerializedName("id_poin_balance")
    int id;
    @SerializedName("nominal")
    int nominal;
    @SerializedName("prev_balance")
    int prevBalance;
    @SerializedName("next_balance")
    int nextBalance;
    @SerializedName("status")
    String status;
    @SerializedName("created_date")
    String createdDate;

    public int getIdMember() {
        return idMember;
    }

    public void setIdMember(int idMember) {
        this.idMember = idMember;
    }

    @SerializedName("id_member")
    int idMember;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @SerializedName("description")
    String description;

    public Poin(){

    }
    protected Poin(Parcel in) {
        id = in.readInt();
        nominal = in.readInt();
        prevBalance = in.readInt();
        nextBalance = in.readInt();
        status = in.readString();
        createdDate = in.readString();
        description = in.readString();
        idMember = in.readInt();
    }

    public static final Creator<Poin> CREATOR = new Creator<Poin>() {
        @Override
        public Poin createFromParcel(Parcel in) {
            return new Poin(in);
        }

        @Override
        public Poin[] newArray(int size) {
            return new Poin[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public int getPrevBalance() {
        return prevBalance;
    }

    public void setPrevBalance(int prevBalance) {
        this.prevBalance = prevBalance;
    }

    public int getNextBalance() {
        return nextBalance;
    }

    public void setNextBalance(int nextBalance) {
        this.nextBalance = nextBalance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(nominal);
        parcel.writeInt(prevBalance);
        parcel.writeInt(nextBalance);
        parcel.writeString(status);
        parcel.writeString(createdDate);
        parcel.writeString(description);
        parcel.writeInt(idMember);
    }
}
