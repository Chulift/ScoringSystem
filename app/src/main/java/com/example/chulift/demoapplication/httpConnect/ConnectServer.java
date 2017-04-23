package com.example.chulift.demoapplication.httpConnect;

import android.util.Log;


import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConnectServer {
    final static String TAG = "ConnectServer";

    public static int connectHttp(String url, RequestBody req) {
        int resp = 0;
        Response response;
        try {
            //final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
            Log.i("start","connecting..");
            Request request = new Request.Builder()
                    .url(url)
                    .post(req)
                    .build();
            OkHttpClient client = new OkHttpClient();
            response = client.newCall(request).execute();
            resp = response.code();
            Log.e("body",response.body().string());//maybe null
        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return resp;
    }
    public static JSONObject getJSONObject(String url,String postBody){
        JSONObject result;
        try {
            final MediaType Json = MediaType.parse("application/json; charset=utf-8");
            Request.Builder builder = new Request.Builder();

            Request request = builder
                    .url(url)
                    .post(RequestBody.create(Json, postBody))
                    .build();

            OkHttpClient client = new OkHttpClient();

            Response response = client.newCall(request).execute();

            Log.d("Status of sever", response.toString());
            try {
                result = new JSONObject(response.body().string());
            } catch (Exception e) {
                Log.e(TAG, "Json Error: " + e.getLocalizedMessage());
                return null;
            }
            Log.d("Value", result.getString("id_template"));
        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(TAG, "Error: " + e.getLocalizedMessage());
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
            return null;
        }
        return result;
    }

    public String getJSONString(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        String resp;
        try {
            Response response = okHttpClient.newCall(request).execute();
            resp = response.body().string();
        } catch (Exception e) {
            resp = "Connect Error:" + e.toString();
        }
        return resp;

    }
}
