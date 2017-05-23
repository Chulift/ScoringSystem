package com.example.chulift.demoapplication.template;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.example.chulift.demoapplication.classes.Utilities;
import com.example.chulift.demoapplication.template.Fragments.EditTemplateFragment;
import com.example.chulift.demoapplication.template.Fragments.ShowTemplateFragment;
import com.example.chulift.demoapplication.classes.Template;
import com.example.chulift.demoapplication.R;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditTemplateActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    protected Template template;
    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user)
    TextView user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_template);
        ButterKnife.bind(this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            String jsonObject = b.getString("template");
            template = new Gson().fromJson(jsonObject, Template.class);
            Log.i("template_path", template.getTemplatePath());
        }
        Utilities.setToolbar(this);
    }

    @Override
    public void onBackPressed() {
        this.back();
    }

    //@OnClick(R.id.back_btn)
    void back() {
        startActivity(new Intent(EditTemplateActivity.this, ShowTemplateListActivity.class));
        finish();
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return ShowTemplateFragment.newInstance(0, template);
            else
                return EditTemplateFragment.newInstance(1, template);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "รูปเทมเพลท";
                case 1:
                    return "แก้ไขข้อมูล";
            }
            return null;
        }
    }
}