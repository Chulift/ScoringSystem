package com.example.chulift.demoapplication.examStorage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.chulift.demoapplication.MenusActivity;
import com.example.chulift.demoapplication.adapter.ExamStorageAdapter;
import com.example.chulift.demoapplication.classes.ConvertJSONString;
import com.example.chulift.demoapplication.classes.Utilities;
import com.example.chulift.demoapplication.config.Config;
import com.example.chulift.demoapplication.R;
import com.example.chulift.demoapplication.httpConnect.ConnectServer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageExamStorageActivity extends AppCompatActivity {
    private final String url = Config.serverUrl + Config.projectName + "getJSON.php";
    @BindView(R.id.recyclerView1)
    RecyclerView recyclerView;
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
        initial();
        Utilities.setToolbar(this);
    }

    @Override
    public void onBackPressed() {
        this.back();
    }

    @OnClick(R.id.back_btn)
    void back() {
        Intent intent = new Intent(this, MenusActivity.class);
        startActivity(intent);
        finish();
    }

    private void initial() {
        ArrayList arrayList = ConvertJSONString.getExamStorageArray(new ConnectServer().getJSONString(url));
        try {
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(new ExamStorageAdapter(arrayList));
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