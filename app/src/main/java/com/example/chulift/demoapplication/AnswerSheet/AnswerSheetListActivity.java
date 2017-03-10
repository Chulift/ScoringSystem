package com.example.chulift.demoapplication.AnswerSheet;

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
import com.example.chulift.demoapplication.ExamSet.ManageExamSetActivity;
import com.example.chulift.demoapplication.R;
import com.example.chulift.demoapplication.httpConnect.ConnectServer;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnswerSheetListActivity extends AppCompatActivity {

    private final String url = Config.serverURL + "getAnswerSheet.php";
    @BindView(R.id.listView1)
    ListView lstView1;
    private ArrayList arrayList;
    private String resp;
    private ExamStorage examStorage;

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
        ButterKnife.bind(this);
        init();
        Utilities.setToolbar(this);
    }

    private void init() {
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
                    lstView1.setAdapter(new Adapter(AnswerSheetListActivity.this, s, "AnswerSheetListActivity"));
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
            }

        }.execute();


    }

    @OnClick(R.id.back_btn)
    void back() {
        Intent intent = new Intent(getApplicationContext(), ManageExamSetActivity.class);
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
}
