package com.lapaksembako.app.api_model;

import com.google.gson.annotations.SerializedName;
import com.lapaksembako.app.model.Faq;
import com.lapaksembako.app.model.User;

import java.util.ArrayList;
import java.util.List;

public class GetFaq {
    @SerializedName("status")
    String status;

    @SerializedName("data")
    List<Faq> faqs;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Faq> getFaqs() {
        return faqs;
    }

    public void setFaqs(List<Faq> faqs) {
        this.faqs = faqs;
    }
}
