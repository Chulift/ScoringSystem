package com.example.chulift.demoapplication.Class;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Chulift on 11/15/2016.
 */

public class ExamStorage {
    private String id_examStorage, numScore, id_answer;
    private String id_template, exam_storage_name, user_email, user_input_template_name;

    public ExamStorage(JSONObject jsonObject) {
        if (jsonObject != null) {
            try {
                id_answer = jsonObject.getString("id_answer");
                user_input_template_name = jsonObject.getString("user_input_template_name");
                id_examStorage = jsonObject.getString("id_examstorage");
                id_template = jsonObject.getString("id_template");
                user_email = jsonObject.getString("user_email");
                exam_storage_name = jsonObject.getString("exam_storage_name");
                numScore = jsonObject.getString("num_score");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getExam_storage_name() {
        return exam_storage_name;
    }

    public String getUser_input_template_name() {
        return user_input_template_name;
    }

    public String getId_answer() {
        return id_answer;
    }

    public String getId_examStorage() {
        return id_examStorage;
    }

    public String answersheet_name() {
        return exam_storage_name;
    }

    public String getId_template() {
        return id_template;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getNumScore() {
        return numScore;
    }

}
