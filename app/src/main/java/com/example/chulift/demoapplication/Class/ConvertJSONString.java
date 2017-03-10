package com.example.chulift.demoapplication.Class;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chulift on 3/6/2017.
 */

public class ConvertJSONString {
    public static ArrayList getTemplateArray(String resp) {
        ArrayList arrayList = new ArrayList();
        try {
            JSONArray jsonArray = new JSONArray(resp);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Template template = new Template(jsonObject);
                arrayList.add(template);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
    public static ArrayList getExamStorageArray(String resp) {
        ArrayList arrayList = new ArrayList();
        try {
            JSONArray jsonArray = new JSONArray(resp);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ExamStorage examStorage = new ExamStorage(jsonObject);
                //create new Item from JSONObject
                arrayList.add(examStorage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
    public static ArrayList getAnswerSheetArray(String resp) {

        ArrayList arrayList = new ArrayList();
        try {
            JSONArray jsonArray = new JSONArray(resp);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                AnswerSheet answerSheet = new AnswerSheet(jsonObject);

                //create new Item from JSONObject
                arrayList.add(answerSheet);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
