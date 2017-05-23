package com.example.chulift.demoapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.chulift.demoapplication.classes.AnswerSheetCamera;
import com.example.chulift.demoapplication.classes.Utilities;
import com.example.chulift.demoapplication.config.Config;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.ip_server)
    EditText serverUrl;
    @BindView(R.id.project_name)
    EditText projectName;
    @BindView(R.id.radio_camera1)
    RadioButton camera1;
    @BindView(R.id.radio_camera2)
    RadioButton camera2;
    AnswerSheetCamera mCamera;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        Utilities.setToolbar(this);
        sp = getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        String camera = sp.getString("answerSheetCamera", AnswerSheetCamera.CAMERA_1.name());
        mCamera = AnswerSheetCamera.valueOf(camera);
        if (mCamera == AnswerSheetCamera.CAMERA_2)
            camera2.setChecked(true);
        else camera1.setChecked(true);
        serverUrl.setText(Config.serverUrl);
        projectName.setText(Config.projectName);
    }

    @OnClick(R.id.update_button)
    void update() {
        sp = getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("serverUrl", serverUrl.getText().toString());
        editor.putString("projectName", projectName.getText().toString());
        editor.putString("answerSheetCamera", mCamera.name());
        editor.apply();

        String defaultServerUrl = Config.serverUrl;
        String defaultProjectName = Config.projectName;

        String newServer = sp.getString("serverUrl", defaultServerUrl);
        String newProjectName = sp.getString("projectName", defaultProjectName);
        Config.setServerUrl(newServer);
        Config.setProjectName(newProjectName);

        new AlertDialog.Builder(this)
                .setTitle("Success!")
                .setMessage("แก้ไขเรียบร้อย")
                .setPositiveButton("ตกลง", null)
                .show();
    }

    @OnClick(R.id.back_btn)
    void back() {
        Intent intent = new Intent(getApplicationContext(), MenusActivity.class);
        startActivity(intent);
        finish();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radio_camera1:
                if (checked) mCamera = AnswerSheetCamera.CAMERA_1;
                break;
            case R.id.radio_camera2:
                if (checked) mCamera = AnswerSheetCamera.CAMERA_2;
                break;
        }
        System.out.println(mCamera.name());
    }

    @Override
    public void onBackPressed() {
        back();
    }
}
