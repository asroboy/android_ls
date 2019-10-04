package com.lapaksembako.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Balance implements Parcelable {
    @SerializedName("id_balance")
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

    @SerializedName("id_member")
    int idMember;

    public int getIdMember() {
        return idMember;
    }

    public void setIdMember(int idMember) {
        this.idMember = idMember;
    }

    public Balance(){

    }

    protected Balance(Parcel in) {
        id = in.readInt();
        nominal = in.readInt();
        prevBalance = in.readInt();
        nextBalance = in.readInt();
        status = in.readString();
        createdDate = in.readString();
        idMember = in.readInt();
    }

    public static final Creator<Balance> CREATOR = new Creator<Balance>() {
        @Override
        public Balance createFromParcel(Parcel in) {
            return new Balance(in);
        }

        @Override
        public Balance[] newArray(int size) {
            return new Balance[size];
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
        parcel.writeInt(idMember);
    }
}
