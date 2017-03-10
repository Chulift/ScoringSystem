package com.example.chulift.demoapplication.httpConnect;


import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.example.chulift.demoapplication.R;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Chulift on 11/13/2016.
 */

public class ConnectServer {
    final static String TAG = "ConnectServer";

    public static int connectHttp(String url, RequestBody req) {
        int resp = 0;
        Response response = null;
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
        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(TAG, "Other Error: " + e.getLocalizedMessage()+"::"+response.body().string());
        }
        finally {

            return resp;
        }
    }
    public static void connectHttp2(Context context, String url, RequestBody req) {
        String result;

        try {
            //final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

            Request request = new Request.Builder()
                    .url(url)
                    .post(req)
                    .build();

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10,TimeUnit.SECONDS)
                    .readTimeout(10,TimeUnit.SECONDS).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    else {
                        String[] stringResponse;
                        Headers responseHeaders = response.headers();
                        stringResponse = new String[responseHeaders.size()];
                        for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                            stringResponse[i] += responseHeaders.name(i) + ": " + responseHeaders.value(i);
                            Log.i("responseString", stringResponse[i]);
                        }

                        Log.i("response", response.body().string());

                    }

                }
            });


        } catch (Exception e) {
            //Log.e(TAG, "Other Error: " + e.getLocalizedMessage()+"::"+response.body().string());
        }
        finally {
        }
    }
    public static String updateObject(String url,String postBody){
        String result = null;
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
                result = response.body().string();
            } catch (Exception e) {
                Log.e(TAG, "Json Error: " + e.getLocalizedMessage());
                return null;
            }
            Log.d("Value", result);
        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(TAG, "Error: " + e.getLocalizedMessage());
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
            return null;
        }
        return result;
    }
    public static JSONObject getJSONObject(String url,String postBody){
        JSONObject result = null;
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
        String resp = "RESPONSE";
        try {
            Response response = okHttpClient.newCall(request).execute();
            resp = response.body().string();
        } catch (Exception e) {
            resp = "Connect Error:" + e.toString();
        } finally {
            return resp;
        }
    }
}
