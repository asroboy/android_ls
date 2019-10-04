package com.lapaksembako.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Address implements Parcelable {
    @SerializedName("id_address")
    int id;
    @SerializedName("id_province")
    int provinsiId;
    @SerializedName("id_city")
    int cityId;
    @SerializedName("address")
    String address;
    @SerializedName("postal_code")
    String postalCode;
    @SerializedName("phone")
    String phone;
    @SerializedName("name_acc")
    String accName;
    boolean isDefault;


    public Address() {

    }

    protected Address(Parcel in) {
        id = in.readInt();
        provinsiId = in.readInt();
        cityId = in.readInt();
        address = in.readString();
        postalCode = in.readString();
        phone = in.readString();
        accName = in.readString();
        isDefault = in.readByte() != 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinsiId() {
        return provinsiId;
    }

    public void setProvinsiId(int provinsiId) {
        this.provinsiId = provinsiId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(provinsiId);
        parcel.writeInt(cityId);
        parcel.writeString(address);
        parcel.writeString(postalCode);
        parcel.writeString(phone);
        parcel.writeString(accName);
        parcel.writeByte((byte) (isDefault ? 1 : 0));
    }
}
