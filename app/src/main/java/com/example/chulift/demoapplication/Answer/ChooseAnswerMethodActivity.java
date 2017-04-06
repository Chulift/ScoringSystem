package com.example.chulift.demoapplication.Answer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.example.chulift.demoapplication.Class.ExamStorage;
import com.example.chulift.demoapplication.Class.Utilities;
import com.example.chulift.demoapplication.ExamStorage.ManageExamStorageActivity;
import com.example.chulift.demoapplication.R;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseAnswerMethodActivity extends AppCompatActivity {

    private ExamStorage examStorage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user)
    TextView user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_create_answer_method);
        String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("examStorage");
            examStorage = new Gson().fromJson(jsonMyObject, ExamStorage.class);
        } else {
            Log.d("Gson", "Fail");
        }
        ButterKnife.bind(this);
        Utilities.setToolbar(this);
    }

    @OnClick(R.id.btn1)
    void CaptureAnswer() {
        Intent intent = new Intent(this, CaptureAnswerActivity.class);
        intent.putExtra("examStorage", new Gson().toJson(examStorage));
        intent.putExtra("previousPage", "ChooseAnswerMethodActivity");
        this.startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn2)
    void selectAnswer() {
        Intent intent = new Intent(this, SelectAnswerActivity.class);
        intent.putExtra("examStorage", new Gson().toJson(examStorage));
        intent.putExtra("previousPage", "ChooseAnswerMethodActivity");
        this.startActivity(intent);
        finish();
    }

    @OnClick(R.id.back_btn)
    void back() {
        Intent intent = new Intent(ChooseAnswerMethodActivity.this, ManageExamStorageActivity.class);
        startActivity(intent);
        finish();
    }
}
