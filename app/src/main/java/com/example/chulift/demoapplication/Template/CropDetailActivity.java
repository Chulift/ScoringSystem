package com.example.chulift.demoapplication.Template;

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

import com.example.chulift.demoapplication.Class.Utilities;
import com.example.chulift.demoapplication.DrawRect.SimpleDrawingView;
import com.example.chulift.demoapplication.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CropDetailActivity extends AppCompatActivity {

    private Uri imageUri = null;
    private String imagePath = null;

    private Bitmap photo, cropPhoto;
    private float startX, startY, width, height, widthOfImage, heightOfImage;
    private float templateStartXRate, templateStartYRate, templateWidthRate, templateHeightRate,
            idStartXRate, idStartYRate, idWidthRate, idHeightRate,
            detailStartXRate, detailStartYRate, detailWidthRate, detailHeightRate;
    @BindView(R.id.imageView)
    SimpleDrawingView imageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user)
    TextView user;
    @BindView(R.id.cropStartBtn)
    Button cropStartBtn;
    @BindView(R.id.reCropBtn)
    Button reCropBtn;
    @BindView(R.id.nextBtn)
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_detail);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        nextBtn.setEnabled(false);
        reCropBtn.setEnabled(false);
        if (extras != null) {
            imageUri = (Uri) extras.get("imageUri");
            imagePath = extras.getString("imagePath");
            templateStartXRate = (float) extras.get("templateStartXRate");
            templateStartYRate = (float) extras.get("templateStartYRate");
            templateWidthRate = (float) extras.get("templateWidthRate");
            templateHeightRate = (float) extras.get("templateHeightRate");
            idStartXRate = (float) extras.get("idStartXRate");
            idStartYRate = (float) extras.get("idStartYRate");
            idWidthRate = (float) extras.get("idWidthRate");
            idHeightRate = (float) extras.get("idHeightRate");
            setBitmap();
            Log.i("Extras", imageUri.toString() + ", " + imagePath);
        }
        Utilities.setToolbar(this);
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
            detailStartXRate = sX / widthOfImage;
            detailStartYRate = sY / heightOfImage;
            detailWidthRate = iw / widthOfImage;
            detailHeightRate = ih / heightOfImage;

            Log.w("Values", startX + "," + startY + "," + width + "," + height);
            Log.w("Values", sX + "," + sY + "," + iw + "," + ih);

            Toast.makeText(this, "เลือกส่วนข้อมูลเรียบร้อยแล้ว", Toast.LENGTH_LONG).show();
            imageView.setCropped(true);
            cropStartBtn.setEnabled(false);
            reCropBtn.setEnabled(true);
            nextBtn.setEnabled(true);

        }
    }

    @OnClick(R.id.reCropBtn)
    void reCrop() {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                return photo;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                imageView.setCropped(false);
                cropStartBtn.setEnabled(true);
                reCropBtn.setEnabled(false);
                nextBtn.setEnabled(false);
            }
        }.execute();
    }

    @OnClick(R.id.back_btn)
    void back() {
        Intent intent = new Intent(this, CropIDActivity.class);
        if (imageUri != null && imagePath != null) {
            intent.putExtra("imageUri", imageUri);
            intent.putExtra("imagePath", imagePath);
            intent.putExtra("templateStartXRate", templateStartXRate);
            intent.putExtra("templateStartYRate", templateStartYRate);
            intent.putExtra("templateWidthRate", templateWidthRate);
            intent.putExtra("templateHeightRate", templateHeightRate);
        }
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.nextBtn)
    void next() {
        if (imageUri != null && imagePath != null) {
            Intent intent = new Intent(this, FillFormActivity.class);
            intent.putExtra("imageUri", imageUri);
            intent.putExtra("imagePath", imagePath);
            intent.putExtra("templateStartXRate", templateStartXRate);
            intent.putExtra("templateStartYRate", templateStartYRate);
            intent.putExtra("templateWidthRate", templateWidthRate);
            intent.putExtra("templateHeightRate", templateHeightRate);
            intent.putExtra("idStartXRate", idStartXRate);
            intent.putExtra("idStartYRate", idStartYRate);
            intent.putExtra("idWidthRate", idWidthRate);
            intent.putExtra("idHeightRate", idHeightRate);
            intent.putExtra("detailStartXRate", detailStartXRate);
            intent.putExtra("detailStartYRate", detailStartYRate);
            intent.putExtra("detailWidthRate", detailWidthRate);
            intent.putExtra("detailHeightRate", detailHeightRate);
            startActivity(intent);
            finish();
        }
    }
}
