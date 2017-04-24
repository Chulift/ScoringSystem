package com.example.chulift.demoapplication.classes;
import org.json.JSONException;
import org.json.JSONObject;


public class AnswerSheet {
    String answerSheetID,examStorageID,status,imagePath,score,answerSheetAnswer;
    public AnswerSheet(JSONObject jsonObject){
        if (jsonObject != null) {
            try {
                answerSheetID = jsonObject.getString("id_answersheet");
                examStorageID = jsonObject.getString("id_examstorage");
                status = jsonObject.getString("status");
                imagePath = jsonObject.getString("image_path");
                score = jsonObject.getString("score");
                answerSheetAnswer = jsonObject.getString("answerSheet_answer");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getScore() {
        return score;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getAnswerSheetAnswer() {
        return answerSheetAnswer;
    }

    public String getAnswerSheetID() {
        return answerSheetID;
    }

    public String getExamStorageID() {
        return examStorageID;
    }

    public String getStatus() {
        return status;
    }
}
