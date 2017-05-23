package com.example.chulift.demoapplication.classes;

import org.json.JSONException;
import org.json.JSONObject;


public class AnswerSheet {
    private String answerSheetID, status, imagePath, score, answer, studentCode;

    public AnswerSheet(JSONObject jsonObject) {
        if (jsonObject != null) {
            try {
                answerSheetID = jsonObject.getString("id_answersheet");
                status = jsonObject.getString("status");
                imagePath = jsonObject.getString("image_path");
                score = jsonObject.getString("score");
                answer = jsonObject.getString("answerSheet_answer");
                studentCode = jsonObject.getString("StudentCode");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getAnswer() {
        return answer;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public String getScore() {
        return score;
    }

    public String getAnswerSheetID() {
        return answerSheetID;
    }

    public String getStatus() {
        return status;
    }
}
