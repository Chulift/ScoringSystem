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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chulift.demoapplication.drawRect.SimpleDrawingView;
import com.example.chulift.demoapplication.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.chulift.demoapplication.classes.Utilities.setToolbar;

public class CropTemplateActivity extends AppCompatActivity {


    private Uri imageUri = null;
    private String imagePath = null;

    private Bitmap photo;
    private float startX, startY, width, height, widthOfImage, heightOfImage;
    private float templateStartXRate, templateStartYRate, templateWidthRate, templateHeightRate;
    @BindView(R.id.imageView)
    SimpleDrawingView imageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user)
    TextView user;
    @BindView(R.id.nextBtn)
    Button nextBtn;
    @BindView(R.id.reCropBtn)
    Button reCropBtn;
    @BindView(R.id.cropStartBtn)
    Button cropStartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_template);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        nextBtn.setEnabled(false);
        reCropBtn.setEnabled(false);
        if (extras != null) {
            imageUri = (Uri) extras.get("imageUri");
            imagePath = extras.getString("imagePath");
            setBitmap();
            Log.i("Extras", imageUri.toString() + ", " + imagePath);
        }

        setToolbar(this);
    }


    private void setBitmap() {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                try {
                    photo = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    widthOfImage = photo.getWidth();
                    heightOfImage = photo.getHeight();
                } catch (IOException e) {
                    Log.e("setBitmap", e.toString());
                }
                return photo;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        }.execute();
    }

    @OnClick(R.id.cropStartBtn)
    void crop() {
        if (imageView.getStartX() != 0) {
            startX = imageView.getStartX();
            startY = imageView.getStartY();
            width = imageView.getW();
            height = imageView.getH();

            int widthOfImageView = imageView.getWidth();
            int heightOfImageView = imageView.getHeight();

            Log.w("image(W,H)", widthOfImageView + "," + heightOfImageView);
            float scaleX = widthOfImage / widthOfImageView;
            float scaleY = heightOfImage / heightOfImageView;
            Log.w("scale", scaleX + "," + scaleY);
            final float sX = startX * scaleX;
            final float sY = startY * scaleY;
            final float iw = width * scaleX;
            final float ih = height * scaleY;

            //calculate Rate
            templateStartXRate = sX / widthOfImage;
            templateStartYRate = sY / heightOfImage;
            templateWidthRate = iw / widthOfImage;
            templateHeightRate = ih / heightOfImage;

            Log.w("Values", startX + "," + startY + "," + width + "," + height);
            Log.w("Values", sX + "," + sY + "," + iw + "," + ih);

            Toast.makeText(this, "เลือกส่วนคำตอบเรียบร้อยแล้ว", Toast.LENGTH_LONG).show();
            imageView.setCropped(true);
            cropStartBtn.setEnabled(false);
            reCropBtn.setEnabled(true);
            nextBtn.setEnabled(true);
        }
    }

    @OnClick(R.id.reCropBtn)
    void reCrop() {
        imageView.setCropped(false);
        cropStartBtn.setEnabled(true);
        reCropBtn.setEnabled(false);
        nextBtn.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        this.back();
    }

    @OnClick(R.id.back_btn)
    void back() {
        Intent captureTemplate = new Intent(this, CaptureTemplateActivity.class);
        if (imageUri != null && imagePath != null) {
            captureTemplate.putExtra("imageUri", imageUri);
            captureTemplate.putExtra("imagePath", imagePath);
        }
        startActivity(captureTemplate);
        finish();
    }

    @OnClick(R.id.nextBtn)
    void next() {
        if (imageUri != null && imagePath != null) {
            Intent cropTemplate = new Intent(this, CropIDActivity.class);
            cropTemplate.putExtra("imageUri", imageUri);
            cropTemplate.putExtra("imagePath", imagePath);
            cropTemplate.putExtra("templateStartXRate", templateStartXRate);
            cropTemplate.putExtra("templateStartYRate", templateStartYRate);
            cropTemplate.putExtra("templateWidthRate", templateWidthRate);
            cropTemplate.putExtra("templateHeightRate", templateHeightRate);
            startActivity(cropTemplate);
            finish();
        }
    }
}
