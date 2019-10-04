package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.Address;
import com.lapaksembako.app.model.User;

import java.util.ArrayList;
import java.util.List;

public class GetReferral {
    @SerializedName("status")
    String status;
    @SerializedName("message")
    String message;
    @SerializedName("referral_id")
    String referralCode;

    @SerializedName("data")
    List<User> referrers;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public List<User> getReferrers() {
        return referrers;
    }

    public void setReferrers(List<User> referrers) {
        this.referrers = referrers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
