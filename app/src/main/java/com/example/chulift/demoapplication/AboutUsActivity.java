package com.example.chulift.demoapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.chulift.demoapplication.classes.Utilities;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        Utilities.setToolbar(this);
    }
    @OnClick(R.id.home_btn)void back(){
        startActivity(new Intent(this,MenusActivity.class));
        finish();
    }
    @Override
    public void onBackPressed() {
        this.back();
    }
}
