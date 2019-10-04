package com.lapaksembako.app.model;

import com.google.gson.annotations.SerializedName;

public class TransferProof {

    @SerializedName("id_transfer_proof")
    int id;
    @SerializedName("transaction_code")
    String transactionCode;
    @SerializedName("id_member")
    int idMember;
    @SerializedName("bank_name")
    String bankName;
    @SerializedName("bank_account")
    String bankAccount;
    @SerializedName("account_number")
    String accountNumber;
    @SerializedName("nominal")
    int nominal;
    @SerializedName("attachment")
    String attachment;
    @SerializedName("is_verified")
    boolean isVerified;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
