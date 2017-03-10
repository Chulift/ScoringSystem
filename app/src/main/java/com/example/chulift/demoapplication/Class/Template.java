package com.example.chulift.demoapplication.Class;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tang on 11/23/2016.
 */

public class Template {
    private String id_template, user_id_user, template_name, user_input_template_name, template_path, startXRate, startYRate, widthRate, heightRate, numberOfCol, answerPerCol;
    private String numberOfChoice, startXRateCode, startYRateCode, widthRateCode, heightRateCode, startXRateInfo, startYRateInfo, widthRateInfo;
    private String heightRateInfo;
    private Boolean dataIsset = false;

    public String getId_template() {
        return id_template;
    }

    public String getUser_id_user() {
        return user_id_user;
    }

    public String getUser_input_template_name() {
        return user_input_template_name;
    }

    public String getTemplate_name() {
        return template_name;
    }

    public String getTemplate_path() {
        return template_path;
    }

    public String getStartXRate() {
        return startXRate;
    }

    public String getStartYRate() {
        return startYRate;
    }

    public String getWidthRate() {
        return widthRate;
    }

    public String getHeightRate() {
        return heightRate;
    }

    public String getNumberOfCol() {
        return numberOfCol;
    }

    public String getAnswerPerCol() {
        return answerPerCol;
    }

    public String getNumberOfChoice() {
        return numberOfChoice;
    }

    public String getStartXRateCode() {
        return startXRateCode;
    }

    public String getStartYRateCode() {
        return startYRateCode;
    }

    public String getWidthRateCode() {
        return widthRateCode;
    }

    public String getHeightRateCode() {
        return heightRateCode;
    }

    public String getStartXRateInfo() {
        return startXRateInfo;
    }

    public String getStartYRateInfo() {
        return startYRateInfo;
    }

    public String getHeightRateInfo() {
        return heightRateInfo;
    }

    public String getWidthRateInfo() {
        return widthRateInfo;
    }

    public Boolean getDataIsset() {
        return dataIsset;
    }

    public void setAnswerPerCol(String answerPerCol) {
        this.answerPerCol = answerPerCol;
    }

    public void setNumberOfChoice(String numberOfChoice) {
        this.numberOfChoice = numberOfChoice;
    }

    public void setNumberOfCol(String numberOfCol) {
        this.numberOfCol = numberOfCol;
    }

    public Template(JSONObject template) {
        if (template != null) {
            try {
                id_template = template.getString("id_template");
                user_id_user = template.getString("user_id_user");
                template_name = template.getString("template_name");
                user_input_template_name = template.getString("user_input_template_name");
                template_path = template.getString("template_path");
                startXRate = template.getString("startXRate");
                startYRate = template.getString("startYRate");
                widthRate = template.getString("widthRate");
                heightRate = template.getString("heightRate");
                numberOfCol = template.getString("numberOfCol");
                answerPerCol = template.getString("answerPerCol");
                numberOfChoice = template.getString("numberOfChoice");

                startXRateCode = template.getString("startXRateCode");
                startYRateCode = template.getString("startYRateCode");
                widthRateCode = template.getString("widthRateCode");
                heightRateCode = template.getString("heightRateCode");

                startXRateInfo = template.getString("startXRateInfo");
                startYRateInfo = template.getString("startYRateInfo");
                widthRateInfo = template.getString("widthRateInfo");
                heightRateInfo = template.getString("heightRateInfo");

                this.dataIsset = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}