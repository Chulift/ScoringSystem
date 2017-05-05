package com.example.chulift.demoapplication.template;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chulift.demoapplication.image.ImagePossessing;
import com.example.chulift.demoapplication.R;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.chulift.demoapplication.classes.Utilities.getRealPathFromURI;
import static com.example.chulift.demoapplication.classes.Utilities.saveImageFromBitmap;
import static com.example.chulift.demoapplication.classes.Utilities.setToolbar;
import static com.example.chulift.demoapplication.classes.Utilities.setupCamera;

public class CaptureTemplateActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }

    private static final String TAGOpenCV = "CaptureTemplateActivity";

    static {
        if (!OpenCVLoader.initDebug()) {

            Log.d(TAGOpenCV, "OpenCV not loaded");
        } else {
            Log.d(TAGOpenCV, "OpenCV loaded");
        }
    }

    private static final int REQUEST_ACTION_CAMERA = 1;
    private Uri imageUri = null;
    private String imagePath = null;
    private Bitmap photo;
    private Bitmap resultBitmap;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user)
    TextView user;
    @BindView(R.id.nextBtn)
    ImageButton nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_template);
        ButterKnife.bind(this);
        nextBtn.setEnabled(false);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imageUri = (Uri) extras.get("imageUri");
            imagePath = extras.getString("imagePath");
            setBitmap();
            nextBtn.setEnabled(true);
            Log.i("Extras", imageUri.toString() + ", " + imagePath);
        } else {
            imageUri = setupCamera(this);
            Log.d("imageUri", imageUri.toString());
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_ACTION_CAMERA);
        }
        setToolbar(this);
    }

    private void setBitmap() {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                Bitmap temp2 = null;
                try {
                    photo = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    Bitmap temp = ImagePossessing.toGrayScale(photo);
                    Bitmap temp1 = ImagePossessing.totBlackWrite(temp);
                    temp2 = ImagePossessing.cutBackGroundImage(temp1, photo);
                } catch (IOException e) {
                    Log.e("setBitmap", e.toString());
                    Toast.makeText(getApplicationContext(), "เกิดข้อผิดพลาด ถ่ายใหม่อีกครั้ง", Toast.LENGTH_SHORT).show();
                }
                return temp2;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
                resultBitmap = bitmap;
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        this.back();
    }

    @OnClick(R.id.back_btn)
    void back() {
        startActivity(new Intent(this, ShowTemplateListActivity.class));
        finish();
    }

    @OnClick(R.id.nextBtn)
    void next() {
        //save ImageResult
        File file = saveImageFromBitmap(resultBitmap);
        if (file != null) {
            Uri resultImg = Uri.fromFile(file);
            Intent cropTemplate = new Intent(this, CropTemplateActivity.class);
            cropTemplate.putExtra("imageUri", resultImg);
            cropTemplate.putExtra("imagePath", file.getAbsolutePath());
            Log.d("ImgPath", file.getAbsolutePath() + " ");
            Log.d("ImgUri", resultImg.toString() + " ");
            startActivity(cropTemplate);
            finish();
        } else Toast.makeText(this, "No Image", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.captureTemplateBtn)
    void capture() {
        imageUri = setupCamera(this);
        Log.d("imageUri", imageUri.toString());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_ACTION_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ACTION_CAMERA && resultCode == RESULT_OK) {
            setBitmap();
            try {
                imagePath = getRealPathFromURI(this, imageUri);
            } catch (Exception e) {
                Log.e("onResult", "Can't get Path.");
            }
            if (imagePath != null && imageUri != null) {
                nextBtn.setEnabled(true);
            }
        }
    }
}