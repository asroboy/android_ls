package com.lapaksembako.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Transaction implements Parcelable {

    @SerializedName("transaction_code")
    String transactionCode;
    @SerializedName("id_member")
    int idMember;

    @SerializedName("id_province")
    int idProvince;

    @SerializedName("id_city")
    int idCity;

    @SerializedName("id_district")
    int idDistrict;

    @SerializedName("id_village")
    int idVillage;

    @SerializedName("address")
    String address;
    @SerializedName("postal_code")
    String postalCode;
    @SerializedName("latitude")
    double latitude;
    @SerializedName("longitude")
    double longitude;
    @SerializedName("note")
    String note;
    @SerializedName("delivery_price")
    int deliveryPrice;
    @SerializedName("total_nominal")
    int totalNominal;
    @SerializedName("payment_status")
    String paymentStatus;
    @SerializedName("payment_date")
    String paymentDate;
    @SerializedName("status")
    String status;
    @SerializedName("created_date")
    String createdDate;
    @SerializedName("updated_date")
    String updatedDate;

    public Transaction() {

    }

    protected Transaction(Parcel in) {
        transactionCode = in.readString();
        idMember = in.readInt();
        idProvince = in.readInt();
        idCity = in.readInt();
        idDistrict = in.readInt();
        idVillage = in.readInt();
        address = in.readString();
        postalCode = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        note = in.readString();
        deliveryPrice = in.readInt();
        totalNominal = in.readInt();
        paymentStatus = in.readString();
        paymentDate = in.readString();
        status = in.readString();
        createdDate = in.readString();
        updatedDate = in.readString();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public int getIdMember() {
        return idMember;
    }

    public void setIdMember(int idMember) {
        this.idMember = idMember;
    }

    public int getIdProvince() {
        return idProvince;
    }

    public void setIdProvince(int idProvince) {
        this.idProvince = idProvince;
    }

    public int getIdCity() {
        return idCity;
    }

    public void setIdCity(int idCity) {
        this.idCity = idCity;
    }

    public int getIdDistrict() {
        return idDistrict;
    }

    public void setIdDistrict(int idDistrict) {
        this.idDistrict = idDistrict;
    }

    public int getIdVillage() {
        return idVillage;
    }

    public void setIdVillage(int idVillage) {
        this.idVillage = idVillage;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(int deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public int getTotalNominal() {
        return totalNominal;
    }

    public void setTotalNominal(int totalNominal) {
        this.totalNominal = totalNominal;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
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

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(transactionCode);
        parcel.writeInt(idMember);
        parcel.writeInt(idProvince);
        parcel.writeInt(idCity);
        parcel.writeInt(idDistrict);
        parcel.writeInt(idVillage);
        parcel.writeString(address);
        parcel.writeString(postalCode);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(note);
        parcel.writeInt(deliveryPrice);
        parcel.writeInt(totalNominal);
        parcel.writeString(paymentStatus);
        parcel.writeString(paymentDate);
        parcel.writeString(status);
        parcel.writeString(createdDate);
        parcel.writeString(updatedDate);
    }
}
