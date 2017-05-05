package com.example.chulift.demoapplication.examStorage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chulift.demoapplication.FirstMenusActivity;
import com.example.chulift.demoapplication.adapter.Adapter;
import com.example.chulift.demoapplication.classes.ConvertJSONString;
import com.example.chulift.demoapplication.classes.Utilities;
import com.example.chulift.demoapplication.config.Config;
import com.example.chulift.demoapplication.MenusActivity;
import com.example.chulift.demoapplication.R;
import com.example.chulift.demoapplication.httpConnect.ConnectServer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageExamStorageActivity extends AppCompatActivity {
    private final String url = Config.projectUrl + "getJSON.php";
    @BindView(R.id.listView1)
    ListView lstView1;
    private ArrayList arrayList;
    private String resp;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user)
    TextView user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_exam_set);
        Utilities.setStrictMode();
        ButterKnife.bind(this);
        init();
        Utilities.setToolbar(this);
    }

    @Override
    public void onBackPressed() {
        this.back();
    }

    @OnClick(R.id.back_btn)
    void back() {
        Intent intent = new Intent(this, FirstMenusActivity.class);
        startActivity(intent);
        finish();
    }

    private void init() {
        resp = new ConnectServer().getJSONString(url);
        arrayList = new ArrayList<>();
        arrayList = ConvertJSONString.getExamStorageArray(resp);
        try {
            lstView1.setAdapter(new Adapter(ManageExamStorageActivity.this, arrayList, "ManageExamStorageActivity"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.add_exam_btn)
    void addExamBtn() {
        startActivity(new Intent(this, CUExamStorageActivity.class));
        finish();
    }
}