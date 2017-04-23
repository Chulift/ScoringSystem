package com.example.chulift.demoapplication.Template;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.chulift.demoapplication.Adapter.RecycleAdapter;
import com.example.chulift.demoapplication.Class.ConvertJSONString;
import com.example.chulift.demoapplication.Class.Template;
import com.example.chulift.demoapplication.Login.LoginActivity;
import com.example.chulift.demoapplication.MenusActivity;
import com.example.chulift.demoapplication.R;
import com.example.chulift.demoapplication.httpConnect.ConnectServer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.chulift.demoapplication.Class.Utilities.setStrictMode;
import static com.example.chulift.demoapplication.Class.Utilities.setToolbar;
import static com.example.chulift.demoapplication.Config.Config.projectUrl;

public class ShowTemplateListActivity extends AppCompatActivity {
    private final String url = projectUrl + "getTemplate.php";

    private ArrayList arrayList;
    private String resp;
    RecycleAdapter adapter;
    @BindView(R.id.image_gallery)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallerry_template);
        setStrictMode();
        ButterKnife.bind(this);
        setToolbar(this);
        init();
    }

    private void init() {
        new LoadGalleryTask().execute();
    }

    private class LoadGalleryTask extends AsyncTask<Void, Integer, ArrayList<Template>> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(ShowTemplateListActivity.this);
            pd.setProgressStyle(R.style.AppTheme_Dark_Dialog);
            pd.setTitle("Loading...");
            pd.setMessage("Loading images...");
            pd.setCancelable(false);
            pd.setIndeterminate(false);
            pd.setMax(100);
            pd.setProgress(0);
            pd.show();
        }

        @Override
        protected ArrayList<Template> doInBackground(Void... voids) {
            try {
                synchronized (this) {
                    resp = new ConnectServer().getJSONString(url);
                    arrayList = new ArrayList<>();
                    arrayList = ConvertJSONString.getTemplateArray(resp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return arrayList;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(ArrayList<Template> templates) {
            try {
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new RecycleAdapter(ShowTemplateListActivity.this, arrayList);
                recyclerView.setAdapter(adapter);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "ผิดพลาด : ไม่สามารถโหลดข้อมูลเทมเพลทได้", Toast.LENGTH_LONG).show();
            }
            pd.dismiss();
        }
    }

    @OnClick(R.id.add_template_btn)
    void addTemplate() {
        startActivity(new Intent(this, CaptureTemplateActivity.class));
        finish();
    }

    @OnClick(R.id.back_btn)
    void back() {
        Intent mainMenusIntent = new Intent(this, MenusActivity.class);
        startActivity(mainMenusIntent);
        finish();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MenusActivity.class);
        startActivity(intent);
        finish();
    }
}
