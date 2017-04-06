package com.example.chulift.demoapplication.Answer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chulift.demoapplication.Class.ExamStorage;
import com.example.chulift.demoapplication.Adapter.SelectAnswerAdapter;
import com.example.chulift.demoapplication.Class.Answer;
import com.example.chulift.demoapplication.Class.Utilities;
import com.example.chulift.demoapplication.Config.Config;
import com.example.chulift.demoapplication.ExamStorage.ManageExamStorageActivity;
import com.example.chulift.demoapplication.Login.LoginActivity;
import com.example.chulift.demoapplication.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
    private final String createAnswerURL = Config.serverURL + "createAnswer.php";
    private final String getAnswerURL = Config.serverURL + "getAnswer.php";

    //private Template template;
    private String previousPage;
    private ExamStorage examStorage;
    private Boolean isEmptyTemplate;
    Answer answer;
    ListView listView1;
    int numOfanswer = 0;
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
            //previousPage = extras.getString("previousPage");

            if (examStorage.getId_answer().equals("null"))
                idAnswer = 0;
            else
                idAnswer = Integer.parseInt(examStorage.getId_answer());
            Log.i("idAnswer", idAnswer + "");
            numOfanswer = Integer.parseInt(examStorage.getNumScore());
        } else {
            Log.d("Gson", "Fail");
            isEmptyTemplate = true;
        }

        array_a = new int[numOfanswer];
        if (idAnswer != 0) {
            String postbody = "{\"id_answer\":\"" + idAnswer + "\"}";
            GetAnswerAsync getAnswerAsync = new GetAnswerAsync(getAnswerURL, postbody);
            getAnswerAsync.execute();
        } else {
            //Create Touch_Answer
            listView1 = (ListView) findViewById(R.id.listView5);
            ArrayList<Integer> arrayList1 = new ArrayList<Integer>();
            try {
                for (int i = 0; i < numOfanswer; i++) {
                    arrayList1.add(i, i);
                }
                listView1.setAdapter(new SelectAnswerAdapter(this, arrayList1,Integer.parseInt(examStorage.getNumChoice())));

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
        String examStoragesID = examStorage.getId_examStorage();
        String email = LoginActivity.getUser().getEmail();
        String scoreExamSet = numOfanswer + "";
        //Intent intent = new Intent(getApplicationContext(), ShowSelectedAnswerActivity.class);
        //intent.putExtra("array", array_a);
        String postbody = "{\"user_email\":\"" + email + "\", \"id_exam_storage\":\"" + examStoragesID + "\", \"num_score\":\"" + scoreExamSet + "\", \"id_answer\":\"" + idAnswer + "\", \"answer\":\"" + new Gson().toJson(array_a) + "\"}";
        SendDataAsync sendDataAsync = new SendDataAsync(createAnswerURL, postbody);
        sendDataAsync.execute();
        //intent.putExtra("myObject", new Gson().toJson(template));
        //startActivity(intent);
        //finish();
    }

    public class GetAnswerAsync extends AsyncTask<Object, Object, String> {
        private final String TAG = "GetTemplateAsync";
        String Url, postBody;
        String result;

        public GetAnswerAsync(String url, String postBody) {
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
            if (resp != "fail") {
                try {
                    JSONArray jsonArray = new JSONArray(resp);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    answer = new Answer(jsonObject);
                    Log.e("answer", answer.getAnswer());

                } catch (Exception e) {
                    Log.e("Create answer", "Create answer fail.");
                }
                try {
                    array_a = new Gson().fromJson(answer.getAnswer(), int[].class);
                    Log.e("array_a", array_a + "");
                    if (numOfanswer > array_a.length) array_a = new int[numOfanswer];
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
                listView1 = (ListView) findViewById(R.id.listView5);
                ArrayList<Integer> arrayList1 = new ArrayList<Integer>();
                try {
                    for (int i = 0; i < numOfanswer; i++) {
                        arrayList1.add(i, i);
                    }
                    listView1.setAdapter(new SelectAnswerAdapter(SelectAnswerActivity.this, arrayList1,Integer.parseInt(examStorage.getNumChoice())));

                } catch (Exception e) {
                    Log.d("Create Answer", "Fail to create AnswerAdapter" + e.getMessage());
                }
            }
        }
    }

    public class SendDataAsync extends AsyncTask<Object, Object, JSONObject> {
        private final String TAG = "SendDataAsync";
        String Url, postBody;
        String result;

        public SendDataAsync(String url, String postBody) {
            this.Url = url;
            this.postBody = postBody;
        }

        @Override
        protected JSONObject doInBackground(Object... params) {
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

                result = response.body().string();
                Log.d("Value", result);
            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e(TAG, "Error: " + e.getLocalizedMessage());
                return null;
            } catch (Exception e) {
                Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
                return null;
            }

            return null;

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
        protected void onPostExecute(JSONObject jsonObject) {
            Log.d("result", result);
            if (result.equals("1")) {
                Toast.makeText(SelectAnswerActivity.this, "ทำงานสำเร็จ", Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 10000ms
                        Intent intent = new Intent(SelectAnswerActivity.this, ManageExamStorageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2000);
            } else
                Toast.makeText(SelectAnswerActivity.this, "เกิดข้อผิดพลาด ลองใหม่อีกครั้ง", Toast.LENGTH_SHORT).show();
        }
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