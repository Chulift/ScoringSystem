package com.example.chulift.demoapplication.AnswerSheet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.chulift.demoapplication.Adapter.Adapter;
import com.example.chulift.demoapplication.Class.ExamStorage;
import com.example.chulift.demoapplication.Class.ConvertJSONString;
import com.example.chulift.demoapplication.Class.Utilities;
import com.example.chulift.demoapplication.Config.Config;
import com.example.chulift.demoapplication.ExamStorage.ManageExamStorageActivity;
import com.example.chulift.demoapplication.R;
import com.example.chulift.demoapplication.httpConnect.ConnectServer;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnswerSheetListActivity extends AppCompatActivity {

    private static final String url = Config.serverURL + "getAnswerSheet.php";

    private ListView answerSheetListView;
    private static ArrayList arrayList;
    private String resp;
    private  ProgressDialog progressDialog;
    public static  ExamStorage examStorage;
    private Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_sheet_list);
        String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //jsonMyObject = extras.getString("myObject");
            jsonMyObject = extras.getString("examStorage");
            examStorage = new Gson().fromJson(jsonMyObject, ExamStorage.class);
        }
        else Log.e("Error bundle","Extras = null");
        ButterKnife.bind(this);
        answerSheetListView = (ListView) findViewById(R.id.listView1);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        init();

        Utilities.setToolbar(this);
    }
    public static void updateData(){
    }
    private void init() {
        progressDialog.show();
        Log.e("init", "init");
        new AsyncTask<Void, Void, ArrayList>() {

            @Override
            protected ArrayList doInBackground(Void... params) {
                resp = new ConnectServer().getJSONString(url + "?id=" + examStorage.getId_examStorage());
                Log.e("RESP", resp);
                arrayList = new ArrayList<>();
                arrayList = ConvertJSONString.getAnswerSheetArray(resp);
                return arrayList;
            }

            @Override
            protected void onPostExecute(ArrayList s) {
                Log.e("POST", s.size() + "");
                try {
                    adapter = new Adapter(AnswerSheetListActivity.this, s, "AnswerSheetListActivity");
                    answerSheetListView.setAdapter(adapter);
                    progressDialog.dismiss();

                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
            }

        }.execute();
    }

    @OnClick(R.id.back_btn)
    void back() {
        Intent intent = new Intent(getApplicationContext(), ManageExamStorageActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.add_answer_sheet_btn)
    void addAnswerSheetBtn() {
        //Toast.makeText(this,"ยังไม่มีหน้า",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, CaptureAnswerSheetActivity.class);
        intent.putExtra("examStorage", new Gson().toJson(examStorage));
        startActivity(intent);

        finish();
    }

    @OnClick(R.id.test_camera)void testCamera() {
        Intent intent = new Intent(AnswerSheetListActivity.this,TestCameraActivity.class);
        intent.putExtra("examStorage",new Gson().toJson(examStorage));
        startActivity(intent);
    }
}
