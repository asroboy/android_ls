package com.lapaksembako.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    public User() {
    }

    protected User(Parcel in) {
        id = in.readInt();
        nama = in.readString();
        phone = in.readString();
        email = in.readString();
        password = in.readString();
        referrerId = in.readString();
        referenceId = in.readString();
        poin = in.readInt();
        balance = in.readInt();
        profilePic = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("id_member")
    int id;
    @SerializedName("full_name")
    String nama;
    @SerializedName("phone")
    String phone;
    @SerializedName("email")
    String email;
    @SerializedName("password")
    String password;
    @SerializedName("referrer_id")
    String referrerId;

    @SerializedName("referral_id")
    String referenceId;

    @SerializedName("profile_pic")
    String profilePic;

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @SerializedName("poin")
    int poin;

    @SerializedName("balance")
    int balance;

    public int getPoin() {
        return poin;
    }

    public void setPoin(int poin) {
        this.poin = poin;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReferrerId() {
        return referrerId;
    }

    public void setReferrerId(String referrerId) {
        this.referrerId = referrerId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nama);
        parcel.writeString(phone);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(referrerId);
        parcel.writeString(referenceId);
        parcel.writeInt(poin);
        parcel.writeInt(balance);
        parcel.writeString(profilePic);
    }
}
