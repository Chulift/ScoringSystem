package com.example.chulift.demoapplication.AnswerSheet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.chulift.demoapplication.Class.ExamStorage;
import com.example.chulift.demoapplication.Class.Utilities;
import com.example.chulift.demoapplication.Image.ImagePossessing;
import com.example.chulift.demoapplication.R;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestCameraActivity extends AppCompatActivity {

    @BindView(R.id.test_image)ImageView testImage;
    private ExamStorage examStorage;
    private Uri imageUri = null;
    Bitmap photo;
    private static final int REQUEST_ACTION_CAMERA = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_camera);
        ButterKnife.bind(this);
        String jsonMyObject = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //jsonMyObject = extras.getString("myObject");
            jsonMyObject = extras.getString("examStorage");
            examStorage = new Gson().fromJson(jsonMyObject, ExamStorage.class);
        }
        imageUri = Utilities.setupCamera(this);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent,REQUEST_ACTION_CAMERA);
        Utilities.setToolbar(this);
    }

    @Override
    public void onBackPressed() {
        this.back();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ACTION_CAMERA && resultCode == RESULT_OK){
            setBitmap();
        }
    }
    private void setBitmap() {

        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                Bitmap temp2 = null;
                try {
                    photo = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    Bitmap temp = ImagePossessing.toGrayscale(photo);
                    Bitmap temp1 = ImagePossessing.totBlackWrite(temp);
                    temp2 = ImagePossessing.cutBackGroundImage(temp1, photo);

                } catch (IOException e) {
                    Log.e("setBitmap", e.toString());
                }
                return temp2;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                testImage.setImageBitmap(bitmap);
            }
        }.execute();

    }

    @OnClick(R.id.back_btn)void back(){
        Intent intent = new Intent(TestCameraActivity.this, AnswerSheetListActivity.class);
        intent.putExtra("examStorage", new Gson().toJson(examStorage));
        startActivity(intent);
        finish();
    }
}
