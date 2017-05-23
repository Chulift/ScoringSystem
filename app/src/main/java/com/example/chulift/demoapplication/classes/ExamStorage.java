package com.example.chulift.demoapplication.classes;

import org.json.JSONException;
import org.json.JSONObject;



public class ExamStorage {
    private String examStorageID, maxScore, answerID, examStoragePath, numberOfChoice;
    private String templateID, examStorageName, templateName;

    public ExamStorage(JSONObject jsonObject) {
        if (jsonObject != null) {
            try {
                numberOfChoice = jsonObject.getString("num_choice");
                examStoragePath = jsonObject.getString("storage_path");
                answerID = jsonObject.getString("id_answer");
                templateName = jsonObject.getString("user_input_template_name");
                examStorageID = jsonObject.getString("id_examstorage");
                templateID = jsonObject.getString("id_template");
                examStorageName = jsonObject.getString("exam_storage_name");
                maxScore = jsonObject.getString("num_score");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getNumberOfChoice() {
        return numberOfChoice;
    }

    public String getExamStoragePath() {
        return examStoragePath;
    }

    public String getExamStorageName() {
        return examStorageName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getAnswerID() {
        return answerID;
    }

    public String getExamStorageID() {
        return examStorageID;
    }

    public String getTemplateID() {
        return templateID;
    }

    public String getMaxScore() {
        return maxScore;
    }

}
