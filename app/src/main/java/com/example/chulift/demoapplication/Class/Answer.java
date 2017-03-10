package com.example.chulift.demoapplication.Class;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chulift on 2/19/2017.
 */

public class Answer {
    private String idAnswer, answer, maxScore,userEmail;

    public Answer(JSONObject jsonObject) {
        if (jsonObject != null) {
            try {
                idAnswer = jsonObject.getString("id_answer");
                answer = jsonObject.getString("answer");
                maxScore = jsonObject.getString("max_score");
                userEmail = jsonObject.getString("user_email");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getAnswer() {
        return answer;
    }

    public String getIdAnswer() {
        return idAnswer;
    }

    public String getMaxScore() {
        return maxScore;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
