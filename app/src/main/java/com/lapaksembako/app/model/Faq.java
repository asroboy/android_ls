package com.lapaksembako.app.model;

import com.google.gson.annotations.SerializedName;

public class Faq {
    @SerializedName("id")
    int id;

    @SerializedName("question")
    String question;

    @SerializedName("answer")
    String answer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
