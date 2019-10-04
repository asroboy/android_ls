package com.lapaksembako.app.model;

import com.google.gson.annotations.SerializedName;

public class AkunBank {

    @SerializedName("id_bank_account")
    int id;

    @SerializedName("bank_name")
    String bankName;

    @SerializedName("bank_account")
    String bankAccount;

    @SerializedName("account_number")
    String accountNumber;

    @SerializedName("id_member")
    String idMember;

    public String getIdMember() {
        return idMember;
    }

    public void setIdMember(String idMember) {
        this.idMember = idMember;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
