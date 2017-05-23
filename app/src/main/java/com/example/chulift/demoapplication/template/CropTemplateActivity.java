package com.example.chulift.demoapplication.template;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
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
    private float widthOfImage;
    private float heightOfImage;
    private float templateStartXRate, templateStartYRate, templateWidthRate, templateHeightRate;
    @BindView(R.id.imageView)
    SimpleDrawingView imageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user)
    TextView user;
    @BindView(R.id.nextBtn)
    ImageButton nextBtn;
    @BindView(R.id.reCropBtn)
    ImageButton reCropBtn;
    @BindView(R.id.cropConfirmBtn)
    ImageButton cropConfirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_template);
        ButterKnife.bind(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        Bundle extras = getIntent().getExtras();
        nextBtn.setEnabled(false);
        nextBtn.setImageResource(R.mipmap.default_arrow_next_icon);
        reCropBtn.setEnabled(false);
        reCropBtn.setImageResource(R.mipmap.default_redo_icon);

        if (extras != null) {
            imageUri = (Uri) extras.get("imageUri");
            imagePath = extras.getString("imagePath");
            if (extras.containsKey("imagePath")) Log.e("testContains", imagePath);
            setBitmap();
            if (extras.containsKey("templateStartXRate")) {
                Log.e("containsKey", "has template rate:" + extras.get("templateStartXRate"));
                templateStartXRate = (float) extras.get("templateStartXRate");
                templateStartYRate = (float) extras.get("templateStartYRate");
                templateWidthRate = (float) extras.get("templateWidthRate");
                templateHeightRate = (float) extras.get("templateHeightRate");
            }
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
                if (templateStartXRate != 0) {
                    Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    Canvas canvas = new Canvas(mutableBitmap);
                    imageView.setImageBitmap(mutableBitmap);


                    int imageView_width = mutableBitmap.getWidth();
                    int imageView_height = mutableBitmap.getHeight();
                    float startX = templateStartXRate * imageView_width;
                    float startY = templateStartYRate * imageView_height;
                    float endX = startX + (templateWidthRate * imageView_width);
                    float endY = startY + (templateHeightRate * imageView_height);
                    canvas.drawBitmap(mutableBitmap, null, new Rect(0, 0, imageView.getWidth(), imageView.getHeight()), null);
                    canvas.drawRect(startX, startY, endX, endY, imageView.getDrawPaint());

                    setCropped();
                }
            }
        }.execute();
    }


    @OnClick(R.id.cropConfirmBtn)
    void crop() {
        if (imageView.getStartX() != 0) {
            float startX = imageView.getStartX();
            float startY = imageView.getStartY();
            float width = imageView.getW();
            float height = imageView.getH();

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
            setCropped();
        }
    }

    void setCropped() {
        imageView.setCropped(true);
        cropConfirmBtn.setEnabled(false);
        cropConfirmBtn.setImageResource(R.mipmap.default_confirm_icon);
        reCropBtn.setEnabled(true);
        reCropBtn.setImageResource(R.mipmap.redo_icon);
        nextBtn.setEnabled(true);
        nextBtn.setImageResource(R.mipmap.arrow_next_icon);
    }

    @OnClick(R.id.reCropBtn)
    void startDraw() {
        imageView.setImageBitmap(photo);
        imageView.setCropped(false);
        cropConfirmBtn.setEnabled(true);
        cropConfirmBtn.setImageResource(R.mipmap.confirm_icon);
        reCropBtn.setEnabled(false);
        reCropBtn.setImageResource(R.mipmap.default_redo_icon);
        nextBtn.setEnabled(false);
        nextBtn.setImageResource(R.mipmap.default_arrow_next_icon);
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
            Intent intent = new Intent(this, CropIDActivity.class);
            intent.putExtra("imageUri", imageUri);
            intent.putExtra("imagePath", imagePath);

            intent.putExtra("templateStartXRate", templateStartXRate);
            intent.putExtra("templateStartYRate", templateStartYRate);
            intent.putExtra("templateWidthRate", templateWidthRate);
            intent.putExtra("templateHeightRate", templateHeightRate);

            startActivity(intent);
            finish();
        }
    }
}
