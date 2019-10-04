package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.Promo;
import com.lapaksembako.app.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class GetTransaction {
    @SerializedName("status")
    String status;

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @SerializedName("data")
    List<Transaction> transactions;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
