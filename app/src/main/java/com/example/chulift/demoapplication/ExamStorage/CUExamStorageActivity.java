package com.example.chulift.demoapplication.ExamStorage;

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

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CUExamStorageActivity extends AppCompatActivity {
    private final String templateUrl = Config.projectUrl + "getTemplate.php";
    private final String url = Config.projectUrl + "CreateExamSet.php";
    public static Boolean IssetTemplate = false;
    public static Template templateSet = null;
    public static String IDTemplate = null;
    //0 = add , 1 = edit
    public static int mode = 0;
    ExamStorage examStorage;
    @BindView(R.id.image_gallery)
    RecyclerView gallery;
    RecycleAdapter adapter;
    RecyclerView recyclerView;
    private ArrayList arrayList;
    private String resp;
    @BindView(R.id.num_score_exam_set)
    EditText numScore;
    @BindView(R.id.name_exam_set)
    EditText nameExamStorage;
    @BindView(R.id.num_choice_exam_set)
    EditText numChoice;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user)
    TextView user;
    @BindView(R.id.header)
    TextView header;

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
        numScore.setFilters(new InputFilter[]{new com.example.chulift.demoapplication.Class.InputFilter(Config.minKeyboardInput, Config.maxKeyboardInput)});
        numChoice.setFilters(new InputFilter[]{new com.example.chulift.demoapplication.Class.InputFilter(Config.minChoiceOfTemplate, Config.maxChoiceOfTemplate)});
        init();
        Utilities.setToolbar(this);
    }

    private void init() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView = (RecyclerView) findViewById(R.id.image_gallery);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        if (mode == 1) {
            nameExamStorage.setText(examStorage.getExam_storage_name());
            Log.i("numS", examStorage.getNumScore());
            int n = Integer.parseInt(examStorage.getNumScore());
            numScore.setText("" + n, TextView.BufferType.EDITABLE);
            numChoice.setText(examStorage.getNumChoice(), TextView.BufferType.EDITABLE);
            String postBody = "{\"num_score\":\"" + examStorage.getNumScore() + "\", " +
                    "\"template_name\":\"" + examStorage.getId_template() + "\"}";
            GetTemplateAsync getTemplateAsync = new GetTemplateAsync(templateUrl, postBody);
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
                        adapter = new RecycleAdapter(CUExamStorageActivity.this, arrayList, "CUExamStorageActivity");
                        recyclerView.setAdapter(adapter);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "ผิดพลาด : ไม่สามารถโหลดข้อมูลเทมเพลทได้", Toast.LENGTH_LONG).show();
                    }
                }
            }.execute();
        }
    }

    @Override
    public void onBackPressed() {
        this.back();
    }

    @OnClick(R.id.back_btn)
    void back() {
        Intent mainMenusIntent = new Intent(this, ManageExamStorageActivity.class);
        startActivity(mainMenusIntent);
        finish();
    }

    @OnTextChanged(R.id.name_exam_set)
    void chanceText() {
        //code goes here.
    }

    @OnClick(R.id.confirm_btn)
    void createExam() {//answer_id_answer, user_email, answerSheet_name
        String temp = nameExamStorage.getText().toString();
        if (!temp.equals("")) {
            if (IssetTemplate && templateSet != null) {
                if (mode == 1) {
                    String examStorageID = examStorage.getId_examStorage();
                    String tempName = templateSet.getId_template();
                    String email = LoginActivity.getUser().getEmail();
                    String editName = nameExamStorage.getText().toString();
                    String scoreExamSet = numScore.getText().toString();
                    String choiceExamSet = numChoice.getText().toString();
                    String postBody = "{\"id_template\":\"" + tempName + "\", " +
                            "\"user_email\":\"" + email + "\", " +
                            "\"num_choice\":\"" + choiceExamSet + "\", " +
                            "\"id_exam_storage\":\"" + examStorageID + "\", " +
                            "\"num_score\":\"" + scoreExamSet + "\", " +
                            "\"exam_storage_name\":\"" + editName + "\", " +
                            "\"mode\":\"" + mode + "\"}";

                    SendDataAsync sendDataAsync = new SendDataAsync(url, postBody);
                    sendDataAsync.execute();
                } else if (mode == 0) {
                    String tempName = templateSet.getId_template();
                    String email = LoginActivity.getUser().getEmail();
                    String editName = nameExamStorage.getText().toString();
                    String scoreExamSet = numScore.getText().toString();
                    String choiceExamSet = numChoice.getText().toString();
                    String postBody = "{\"id_template\":\"" + tempName + "\", " +
                            "\"user_email\":\"" + email + "\", " +
                            "\"num_choice\":\"" + choiceExamSet + "\", " +
                            "\"num_score\":\"" + scoreExamSet + "\", " +
                            "\"exam_storage_name\":\"" + editName + "\", " +
                            "\"mode\":\"" + mode + "\"}";

                    SendDataAsync sendDataAsync = new SendDataAsync(url, postBody);
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
        if (Objects.equals(scoreExamSet, "0") || Objects.equals(scoreExamSet, ""))
            scoreExamSet = String.valueOf((Integer.parseInt(templateSet.getNumberOfCol()) * Integer.parseInt(templateSet.getAnswerPerCol())));
        String postBody = "{\"num_score\":\"" + scoreExamSet + "\"}";
        GetTemplateAsync templateAsync = new GetTemplateAsync(templateUrl, postBody);
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
                    adapter = new RecycleAdapter(CUExamStorageActivity.this, arrayList, "CUExamStorageActivity");
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class SendDataAsync extends AsyncTask<Object, Object, Integer> {
        private final String TAG = "SendDataAsync";
        String Url, postBody;
        String result;
        int code = 0;

        public SendDataAsync(String url, String postBody) {
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

                result = response.body().string();
                code = response.code();
                Log.d("Value", result);
            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e(TAG, "Error: " + e.getLocalizedMessage());
                return 0;
            } catch (Exception e) {
                Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
                return 0;
            }
            return code;

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
        protected void onPostExecute(Integer code) {
            if (code == 200) {
                if (mode == 1)
                    Toast.makeText(CUExamStorageActivity.this, "Update success", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(CUExamStorageActivity.this, "Create success", Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 10000ms
                        Intent intent = new Intent(CUExamStorageActivity.this, ManageExamStorageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2000);
            } else if (!Objects.equals(result, "")) {
                try {
                    arrayList = ConvertJSONString.getTemplateArray(result);
                    Log.i("array", "" + arrayList.size());
                    adapter = new RecycleAdapter(CUExamStorageActivity.this, arrayList, "CUExamStorageActivity");
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                Toast.makeText(CUExamStorageActivity.this, "Fail to create exam", Toast.LENGTH_SHORT).show();
        }
    }
}