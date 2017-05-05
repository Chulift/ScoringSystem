package com.example.chulift.demoapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chulift.demoapplication.adapter.Adapter;
import com.example.chulift.demoapplication.classes.Menu;
import com.example.chulift.demoapplication.template.ShowTemplateListActivity;
import com.example.chulift.demoapplication.examStorage.ManageExamStorageActivity;
import com.example.chulift.demoapplication.login.LoginActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenusActivity extends AppCompatActivity {

    private ArrayList<Menu> menus;
    //private String[] names = {"Manage Template", "Grading Exam","Manage Answer","Settings","About us","Log out"};
    private String[] names = {"จัดการกระดาษคำตอบ", "จัดการชุดข้อสอบ", "ตั้งค่า", "เกี่ยวกับเรา", "ออกจากระบบ"};
    private int[] images = {R.drawable.photo_camera, R.drawable.notes, R.drawable.file, R.drawable.team, R.drawable.logout_icon_256};
    private int[] colors = {R.color.colorHuman, R.color.indigo, R.color.blue, R.color.grass, R.color.accent};
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.user)TextView user;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);
        ButterKnife.bind(this);
        init(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        user.setText(user.getText()+LoginActivity.getUser().getName());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void init(final Context context) {
        menus = getMenus();
        try {
            listView.setAdapter(new Adapter(context, menus, "MenusActivity"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(MenusActivity.this, ShowTemplateListActivity.class);

                        break;
                    case 1:
                        intent = new Intent(MenusActivity.this, ManageExamStorageActivity.class);

                        break;
                    case 2:
                        //Toast.makeText(context,"หน้านี้ยังไม่มี",Toast.LENGTH_SHORT).show();
                        intent = new Intent(MenusActivity.this, CameraActivity.class);
                        break;
                    case 3:
                        intent = new Intent(MenusActivity.this,AboutUsActivity.class);

                        break;
                    case 4:
                        intent = new Intent(MenusActivity.this, LoginActivity.class);

                        break;
                    default:
                        Toast.makeText(context, "ยังไม่มีหน้านี้", Toast.LENGTH_SHORT).show();
                        break;
                }
                if(intent != null) {
                    startActivity(intent);
                    finish();
                }
                else Log.i("Intent","null intent");
            }
        });
    }

    private ArrayList<Menu> getMenus() {
        ArrayList<Menu> arrayList = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            arrayList.add(i, new Menu(i + "", names[i], images[i], colors[i]));
        }
        return arrayList;
    }
}
