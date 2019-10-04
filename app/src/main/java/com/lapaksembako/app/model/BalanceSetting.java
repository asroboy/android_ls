package com.lapaksembako.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BalanceSetting implements Parcelable {
    @SerializedName("id_balance_setting")
    int id;

    protected BalanceSetting(Parcel in) {
        id = in.readInt();
        memberFee = in.readInt();
        memberCashback = in.readInt();
        memberFirstOff = in.readInt();
        stockpointFee = in.readInt();
        stockpointCashback = in.readInt();
    }

    public static final Creator<BalanceSetting> CREATOR = new Creator<BalanceSetting>() {
        @Override
        public BalanceSetting createFromParcel(Parcel in) {
            return new BalanceSetting(in);
        }

        @Override
        public BalanceSetting[] newArray(int size) {
            return new BalanceSetting[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberFee() {
        return memberFee;
    }

    public void setMemberFee(int memberFee) {
        this.memberFee = memberFee;
    }

    public int getMemberCashback() {
        return memberCashback;
    }

    public void setMemberCashback(int memberCashback) {
        this.memberCashback = memberCashback;
    }

    public int getMemberFirstOff() {
        return memberFirstOff;
    }

    public void setMemberFirstOff(int memberFirstOff) {
        this.memberFirstOff = memberFirstOff;
    }

    public int getStockpointFee() {
        return stockpointFee;
    }

    public void setStockpointFee(int stockpointFee) {
        this.stockpointFee = stockpointFee;
    }

    public int getStockpointCashback() {
        return stockpointCashback;
    }

    public void setStockpointCashback(int stockpointCashback) {
        this.stockpointCashback = stockpointCashback;
    }

    @SerializedName("member_fee")
    int	memberFee;

    @SerializedName("member_cashback")
    int memberCashback;

    @SerializedName("member_first_off")
    int memberFirstOff;

    @SerializedName("stockpoint_fee")
    int stockpointFee;

    @SerializedName("stockpoint_cashback")
    int stockpointCashback;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(memberFee);
        parcel.writeInt(memberCashback);
        parcel.writeInt(memberFirstOff);
        parcel.writeInt(stockpointFee);
        parcel.writeInt(stockpointCashback);
    }
}
