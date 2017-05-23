package com.example.chulift.demoapplication.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class Answer {
    private String answer;

    public Answer(JSONObject jsonObject) {
        if (jsonObject != null) {
            try {
                answer = jsonObject.getString("answer");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public Answer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }
}
