package com.example.chulift.demoapplication.answer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chulift.demoapplication.classes.ExamStorage;
import com.example.chulift.demoapplication.adapter.SelectAnswerAdapter;
import com.example.chulift.demoapplication.classes.Answer;
import com.example.chulift.demoapplication.classes.Utilities;
import com.example.chulift.demoapplication.config.Config;
import com.example.chulift.demoapplication.examStorage.ManageExamStorageActivity;
import com.example.chulift.demoapplication.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SelectAnswerActivity extends AppCompatActivity {
    public static int[] array_a;
    private final String updateAnswerURL = Config.serverUrl + Config.projectName + "model/answer/updateAnswer.php";
    private final String createAnswerURL = Config.serverUrl + Config.projectName + "model/answer/insertAnswer.php";
    private final String getAnswerURL = Config.serverUrl + Config.projectName + "model/answer/getAnswer.php";

    private ExamStorage examStorage;
    Answer answer;
    ListView listView1;
    int numOfAnswer = 0;
    @BindView(R.id.user)
    TextView user;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private int idAnswer = 0;
    String resp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_answer);
        String jsonMyObject;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("examStorage");
            examStorage = new Gson().fromJson(jsonMyObject, ExamStorage.class);
            if (examStorage.getAnswerID().equals("null"))
                idAnswer = 0;
            else
                idAnswer = Integer.parseInt(examStorage.getAnswerID());
            Log.i("idAnswer", idAnswer + "");
            numOfAnswer = Integer.parseInt(examStorage.getMaxScore());
        } else {
            Log.d("Gson", "Fail");
        }

        array_a = new int[numOfAnswer];
        if (idAnswer != 0) {
            String postbody = "{\"id_answer\":\"" + idAnswer + "\"}";
            GetAnswerAsync getAnswerAsync = new GetAnswerAsync(getAnswerURL, postbody);
            getAnswerAsync.execute();
        } else {
            //Create Touch_Answer
            listView1 = (ListView) findViewById(R.id.listView5);
            ArrayList<Integer> arrayList1 = new ArrayList<>();
            try {
                for (int i = 0; i < numOfAnswer; i++) {
                    arrayList1.add(i, i);
                }
                listView1.setAdapter(new SelectAnswerAdapter(this, arrayList1, Integer.parseInt(examStorage.getNumberOfChoice())));

            } catch (Exception e) {
                Log.d("Create Answer", "Fail to create AnswerAdapter" + e.getMessage());
            }
        }
        ButterKnife.bind(this);
        Utilities.setToolbar(this);
    }

    @OnClick(R.id.capture_answer_btn)
    void capture() {
        Intent intent = new Intent(SelectAnswerActivity.this, CaptureAnswerActivity.class);
        intent.putExtra("examStorage", new Gson().toJson(examStorage));
        startActivity(intent);
    }

    @OnClick(R.id.confirm_btn)
    void confirm() {
        String examStoragesID = examStorage.getExamStorageID();
        String postbody = "{\"id_exam_storage\":\"" + examStoragesID + "\", \"answer\":\"" + new Gson().toJson(array_a) + "\"}";
        String url = idAnswer == 0 ? createAnswerURL : updateAnswerURL;
        SendDataAsync sendDataAsync = new SendDataAsync(url, postbody);
        sendDataAsync.execute();
    }

    private class GetAnswerAsync extends AsyncTask<Object, Object, String> {
        private final String TAG = "GetTemplateAsync";
        String Url, postBody;

        GetAnswerAsync(String url, String postBody) {
            this.Url = url;
            this.postBody = postBody;
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                final MediaType Json = MediaType.parse("application/json; charset=utf-8");
                Request.Builder builder = new Request.Builder();

                Request request = builder
                        .url(Url)
                        .post(RequestBody.create(Json, postBody))
                        .build();

                OkHttpClient client = new OkHttpClient();

                Response response = client.newCall(request).execute();

                Log.d("Status of sever", response.toString());

                resp = response.body().string();
                Log.d("Value", resp);
            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e(TAG, "Error: " + e.getLocalizedMessage());
                return null;
            } catch (Exception e) {
                Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
                return null;
            }
            return resp;

        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resp) {
            if (!Objects.equals(resp, "fail")) {
                try {
                    JSONObject mainJson = new JSONObject(resp);
                    System.out.println(mainJson.toString());

                    //JSONArray jsonArray = mainJson.getJSONArray("answer");
                    //System.out.println(jsonArray.toString());
                    //System.out.println(jsonArray.length());
                    //JSONObject jsonObject = jsonArray.getJSONObject(0);
                    answer = new Answer((String) mainJson.get("answer"));
                    Log.d("answer", answer.getAnswer());

                } catch (Exception e) {
                    Log.e("Create answer", "Create answer fail.(" + e.toString() + ")");
                }
                try {
                    array_a = new Gson().fromJson(answer.getAnswer(), int[].class);
                    Log.d("array_a", Arrays.toString(array_a) + "");
                    if (numOfAnswer > array_a.length) array_a = new int[numOfAnswer];
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
                listView1 = (ListView) findViewById(R.id.listView5);
                ArrayList<Integer> arrayList1 = new ArrayList<>();
                try {
                    for (int i = 0; i < numOfAnswer; i++) {
                        arrayList1.add(i, i);
                    }
                    listView1.setAdapter(new SelectAnswerAdapter(SelectAnswerActivity.this, arrayList1, Integer.parseInt(examStorage.getNumberOfChoice())));

                } catch (Exception e) {
                    Log.d("Create Answer", "Fail to create AnswerAdapter" + e.getMessage());
                }
            }
        }
    }

    private class SendDataAsync extends AsyncTask<Object, Object, Integer> {
        private final String TAG = "SendDataAsync";
        String Url, postBody;
        int result;

        SendDataAsync(String url, String postBody) {
            this.Url = url;
            this.postBody = postBody;
        }

        @Override
        protected Integer doInBackground(Object... params) {
            try {
                final MediaType Json = MediaType.parse("application/json; charset=utf-8");
                Request.Builder builder = new Request.Builder();

                Request request = builder
                        .url(Url)
                        .post(RequestBody.create(Json, postBody))
                        .build();

                OkHttpClient client = new OkHttpClient();

                Response response = client.newCall(request).execute();

                Log.d("Status of sever", response.toString());

                result = response.code();
            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e(TAG, "Error: " + e.getLocalizedMessage());
                return null;
            } catch (Exception e) {
                Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
                return null;
            }

            return result;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer jsonObject) {
            Log.d("response", String.valueOf(result));
            if (result == 200) {
                Toast.makeText(SelectAnswerActivity.this, "ทำงานสำเร็จ", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(SelectAnswerActivity.this, "เกิดข้อผิดพลาด ลองใหม่อีกครั้ง", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        this.back();
    }

    @OnClick(R.id.back_btn)
    void back() {
        Intent intent;
        intent = new Intent(SelectAnswerActivity.this, ManageExamStorageActivity.class);
        intent.putExtra("examStorage", new Gson().toJson(examStorage));
        startActivity(intent);
        finish();
    }
}