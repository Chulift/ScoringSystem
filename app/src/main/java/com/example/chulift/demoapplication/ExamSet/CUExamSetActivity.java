package com.example.chulift.demoapplication.ExamSet;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chulift.demoapplication.Class.ExamStorage;
import com.example.chulift.demoapplication.Adapter.RecycleAdapter;
import com.example.chulift.demoapplication.Class.ConvertJSONString;
import com.example.chulift.demoapplication.Class.Template;
import com.example.chulift.demoapplication.Class.Utilities;
import com.example.chulift.demoapplication.Config.Config;
import com.example.chulift.demoapplication.Login.LoginActivity;
import com.example.chulift.demoapplication.R;
import com.example.chulift.demoapplication.httpConnect.ConnectServer;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CUExamSetActivity extends AppCompatActivity {
    private final String templateUrl = Config.serverURL + "getTemplate.php";
    private final String url = Config.serverURL + "CreateExamSet.php";
    public static Boolean IssetTemplate = false;
    public static Template templateSet = null;
    public static String IDTemplate = null;
    public static int position = -1;
    //0 = add , 1 = edit
    public static int mode = 0;
    ExamStorage examStorage;
    @BindView(R.id.image_gallery)
    RecyclerView gallery;
    RecycleAdapter adapter;
    RecyclerView recyclerView;
    private ArrayList arrayList;
    private String resp;
    public static EditText numScore;
    public static EditText editTxt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user)
    TextView user;
    @BindView(R.id.header)TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam_set);
        ButterKnife.bind(this);
        Utilities.setStrictMode();
        IssetTemplate = false;
        Utilities.hideInputSoftKeyboard(this);
        mode = 0;
        String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            IssetTemplate = true;
            jsonMyObject = extras.getString("examStorage");
            examStorage = new Gson().fromJson(jsonMyObject, ExamStorage.class);
            IDTemplate = examStorage.getId_template();
            Log.e("IDTEmp", IDTemplate);
            mode = 1;
            header.setText("แก้ไขชุดข้อสอบ");
            Log.i("editExamStorage", examStorage.getId_examStorage());
        }
        editTxt = (EditText) findViewById(R.id.name_exam_set);
        numScore = (EditText) findViewById(R.id.num_score_exam_set);
        numScore.setFilters(new InputFilter[]{new com.example.chulift.demoapplication.Class.InputFilter(Config.minKeyboardInput, Config.maxKeyboardInput)});

        init();
        Utilities.setToolbar(this);
    }

    private void init() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView = (RecyclerView) findViewById(R.id.image_gallery);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        if (mode == 1) {
            editTxt.setText(examStorage.getExam_storage_name());
            Log.i("numS", examStorage.getNumScore());
            int n = Integer.parseInt(examStorage.getNumScore());
            numScore.setText("" + n, TextView.BufferType.EDITABLE);
            String postbody = "{\"num_score\":\"" + examStorage.getNumScore() + "\", \"template_name\":\"" + examStorage.getId_template() + "\"}";
            GetTemplateAsync getTemplateAsync = new GetTemplateAsync(templateUrl, postbody);
            getTemplateAsync.execute();

        } else {
            new AsyncTask<Void, Void, ArrayList<Template>>() {
                @Override
                protected ArrayList<Template> doInBackground(Void... voids) {
                    resp = new ConnectServer().getJSONString(templateUrl);
                    arrayList = new ArrayList<>();
                    arrayList = ConvertJSONString.getTemplateArray(resp);
                    return arrayList;
                }

                @Override
                protected void onPostExecute(ArrayList<Template> templates) {
                    try {
                        Log.i("array", "" + arrayList.size());
                        adapter = new RecycleAdapter(CUExamSetActivity.this, arrayList, "CUExamSetActivity");
                        recyclerView.setAdapter(adapter);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "ผิดพลาด : ไม่สามารถโหลดข้อมูลเทมเพลทได้", Toast.LENGTH_LONG).show();
                    }
                }
            }.execute();
        }
    }

    @OnClick(R.id.back_btn)
    void back() {
        Intent mainMenusIntent = new Intent(this, ManageExamSetActivity.class);
        startActivity(mainMenusIntent);
        finish();
    }

    @OnTextChanged(R.id.name_exam_set)
    void chanceText() {
        //code goes here.
    }

    @OnClick(R.id.confirm_btn)
    void createExam() {//answer_id_answer, user_email, answersheet_name
        String temp = editTxt.getText().toString();
        if (!temp.equals("")) {
            if (IssetTemplate && templateSet != null) {
                if (mode == 1) {
                    String examStoragesID = examStorage.getId_examStorage();
                    String tempName = templateSet.getId_template();
                    String email = LoginActivity.getUser().getEmail();
                    String editName = editTxt.getText().toString();
                    String scoreExamSet = numScore.getText().toString();
                    String postbody = "{\"id_template\":\"" + tempName + "\", \"user_email\":\"" + email + "\", \"id_exam_storage\":\"" + examStoragesID + "\", \"num_score\":\"" + scoreExamSet + "\", \"exam_storage_name\":\"" + editName + "\", \"mode\":\"" + mode + "\"}";

                    SendDataAsync sendDataAsync = new SendDataAsync(url, postbody);
                    sendDataAsync.execute();
                } else if (mode == 0) {
                    String tempName = templateSet.getId_template();
                    String email = LoginActivity.getUser().getEmail();
                    String editName = editTxt.getText().toString();
                    String scoreExamSet = numScore.getText().toString();
                    String postbody = "{\"id_template\":\"" + tempName + "\", \"user_email\":\"" + email + "\", \"num_score\":\"" + scoreExamSet + "\", \"exam_storage_name\":\"" + editName + "\", \"mode\":\"" + mode + "\"}";

                    SendDataAsync sendDataAsync = new SendDataAsync(url, postbody);
                    sendDataAsync.execute();
                }
            } else {
                Toast.makeText(this, "เลือกเทมเพลท", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "ใส่ชื่อชุดข้อสอบ", Toast.LENGTH_SHORT).show();
        }

    }

    @OnTextChanged(R.id.num_score_exam_set)
    void getTemplate() {
        //isSelectedChange = true;
        String scoreExamSet = numScore.getText().toString();
        if (scoreExamSet == "0" || scoreExamSet == "")
            scoreExamSet = String.valueOf((Integer.parseInt(templateSet.getNumberOfCol()) * Integer.parseInt(templateSet.getAnswerPerCol())));
        String postbody = "{\"num_score\":\"" + scoreExamSet + "\"}";
        GetTemplateAsync templateAsync = new GetTemplateAsync(templateUrl, postbody);
        templateAsync.execute();
    }

    private class GetTemplateAsync extends AsyncTask<Object, Object, String> {
        private final String TAG = "GetTemplateAsync";
        String Url, postBody;

        public GetTemplateAsync(String url, String postBody) {
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
            if (resp != null) {
                try {
                    arrayList = new ArrayList<>();
                    arrayList = ConvertJSONString.getTemplateArray(resp);
                    Log.i("array", "" + arrayList.size());
                    adapter = new RecycleAdapter(CUExamSetActivity.this, arrayList, "CUExamSetActivity");
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class SendDataAsync extends AsyncTask<Object, Object, JSONObject> {
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
            try {
                return new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
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
            if (result.equals("1")) {
                if (mode == 1)
                    Toast.makeText(CUExamSetActivity.this, "Update success", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(CUExamSetActivity.this, "Create success", Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 10000ms
                        Intent intent = new Intent(CUExamSetActivity.this, ManageExamSetActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2000);
            } else if (result != "") {
                try {
                    arrayList = ConvertJSONString.getTemplateArray(result);
                    Log.i("array", "" + arrayList.size());
                    adapter = new RecycleAdapter(CUExamSetActivity.this, arrayList, "CUExamSetActivity");
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                Toast.makeText(CUExamSetActivity.this, "Fail to create exam", Toast.LENGTH_SHORT).show();
        }
    }
}