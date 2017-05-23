package com.example.chulift.demoapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.chulift.demoapplication.adapter.MenuAdapter;
import com.example.chulift.demoapplication.classes.Menu;
import com.example.chulift.demoapplication.config.Config;
import com.example.chulift.demoapplication.login.LoginActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.chulift.demoapplication.classes.Utilities.setToolbar;

public class MenusActivity extends AppCompatActivity {

    private String[] names = {"จัดการกระดาษคำตอบ", "จัดการชุดข้อสอบ", "ตั้งค่า", "เกี่ยวกับเรา", "ออกจากระบบ"};
    private int[] images = {R.drawable.photo_camera, R.drawable.notes, R.drawable.file, R.drawable.team, R.drawable.logout_icon_256};
    private int[] colors = {R.color.colorHuman, R.color.indigo, R.color.blue, R.color.grass, R.color.accent};
    @BindView(R.id.menu_gallery)
    RecyclerView recyclerView;
    MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_menus_acticity);
        setToolbar(this);
        ButterKnife.bind(this);
        initial();
    }

    private void setUpServer() {
        SharedPreferences sharedPreferences = getSharedPreferences("CONFIG", MODE_PRIVATE);
        String defaultServerUrl = Config.serverUrl;
        String defaultProjectName = Config.projectName;
        String newServer = sharedPreferences.getString("serverUrl", defaultServerUrl);
        String newProjectName = sharedPreferences.getString("projectName", defaultProjectName);
        Config.setServerUrl(newServer);
        Config.setProjectName(newProjectName);
    }

    private void initial() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        menuAdapter = new MenuAdapter(this, getMenus());
        recyclerView.setAdapter(menuAdapter);
        setUpServer();
    }

    private ArrayList<Menu> getMenus() {
        ArrayList<Menu> arrayList = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            arrayList.add(i, new Menu(i + "", names[i], images[i], colors[i]));
        }
        return arrayList;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
