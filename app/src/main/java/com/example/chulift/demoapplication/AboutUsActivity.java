package com.example.chulift.demoapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
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
