package com.example.chulift.demoapplication.template;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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

import com.example.chulift.demoapplication.classes.Utilities;
import com.example.chulift.demoapplication.drawRect.SimpleDrawingView;
import com.example.chulift.demoapplication.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CropDetailActivity extends AppCompatActivity {

    private Uri imageUri = null;
    private String imagePath = null;

    private Bitmap photo;
    private float widthOfImage;
    private float heightOfImage;
    private float templateStartXRate, templateStartYRate, templateWidthRate, templateHeightRate,
            idStartXRate, idStartYRate, idWidthRate, idHeightRate,
            detailStartXRate, detailStartYRate, detailWidthRate, detailHeightRate;
    @BindView(R.id.imageView)
    SimpleDrawingView imageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user)
    TextView user;
    @BindView(R.id.cropConfirmBtn)
    ImageButton cropStartBtn;
    @BindView(R.id.reCropBtn)
    ImageButton reCropBtn;
    @BindView(R.id.nextBtn)
    ImageButton nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_detail);
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

            templateStartXRate = (float) extras.get("templateStartXRate");
            templateStartYRate = (float) extras.get("templateStartYRate");
            templateWidthRate = (float) extras.get("templateWidthRate");
            templateHeightRate = (float) extras.get("templateHeightRate");

            idStartXRate = (float) extras.get("idStartXRate");
            idStartYRate = (float) extras.get("idStartYRate");
            idWidthRate = (float) extras.get("idWidthRate");
            idHeightRate = (float) extras.get("idHeightRate");

            setBitmap();
            if (extras.containsKey("detailStartXRate")) {
                Log.e("containsKey", "has detail rate:" + extras.get("detailStartXRate"));
                detailStartXRate = (float) extras.get("detailStartXRate");
                detailStartYRate = (float) extras.get("detailStartYRate");
                detailWidthRate = (float) extras.get("detailWidthRate");
                detailHeightRate = (float) extras.get("detailHeightRate");

                setCropped();
            }
            Log.i("Extras", imageUri.toString() + ", " + imagePath);
        }
        Utilities.setToolbar(this);
    }

    private void setBitmap() {
        imageView.setPaintColor(Color.BLUE);
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
                    canvas.drawBitmap(mutableBitmap, null, new Rect(0, 0, imageView.getWidth(), imageView.getHeight()), null);

                    int imageView_width = mutableBitmap.getWidth();
                    int imageView_height = mutableBitmap.getHeight();
                    float startX = templateStartXRate * imageView_width;
                    float startY = templateStartYRate * imageView_height;
                    float endX = startX + (templateWidthRate * imageView_width);
                    float endY = startY + (templateHeightRate * imageView_height);
                    Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    paint.setAntiAlias(true);
                    paint.setStrokeWidth(5);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeJoin(Paint.Join.ROUND);
                    paint.setStrokeCap(Paint.Cap.ROUND);
                    canvas.drawRect(startX, startY, endX, endY, paint);

                    startX = idStartXRate * imageView_width;
                    startY = idStartYRate * imageView_height;
                    endX = startX + (idWidthRate * imageView_width);
                    endY = startY + (idHeightRate * imageView_height);
                    paint.setColor(Color.GREEN);
                    canvas.drawRect(startX, startY, endX, endY, paint);
                    if (detailStartXRate != 0) {
                        startX = detailStartXRate * imageView_width;
                        startY = detailStartYRate * imageView_height;
                        endX = startX + (detailWidthRate * imageView_width);
                        endY = startY + (detailHeightRate * imageView_height);
                        paint.setColor(Color.BLUE);
                        canvas.drawRect(startX, startY, endX, endY, paint);
                        setCropped();
                    }
                }
            }
        }.execute();
    }

    @OnClick(R.id.cropConfirmBtn)
    void crop() {
        if (imageView.getStartX() != 0) {
            if (Utilities.isOverlap(imageView.getStartX(), imageView.getStartY(), imageView.getPointX(), imageView.getPointY(), templateStartXRate, templateStartYRate, templateWidthRate, templateHeightRate, imageView.getWidth(), imageView.getHeight())
                    || Utilities.isOverlap(imageView.getStartX(), imageView.getStartY(), imageView.getPointX(), imageView.getPointY(), idStartXRate, idStartYRate, idWidthRate, idHeightRate, imageView.getWidth(), imageView.getHeight())) {
                Toast.makeText(getApplicationContext(), "ไม่สามารถเลือกทับกับส่วนอื่นได้", Toast.LENGTH_SHORT).show();
                return;
            }
            float detailStartX = imageView.getStartX();
            float detailStartY = imageView.getStartY();
            float width = imageView.getW();
            float height = imageView.getH();

            int widthOfImageView = imageView.getWidth();
            int heightOfImageView = imageView.getHeight();

            Log.w("image(W,H)", widthOfImageView + "," + heightOfImageView);
            float scaleX = widthOfImage / widthOfImageView;
            float scaleY = heightOfImage / heightOfImageView;
            Log.w("scale", scaleX + "," + scaleY);
            final float sX = detailStartX * scaleX;
            final float sY = detailStartY * scaleY;
            final float iw = width * scaleX;
            final float ih = height * scaleY;

            //calculate Rate
            detailStartXRate = sX / widthOfImage;
            detailStartYRate = sY / heightOfImage;
            detailWidthRate = iw / widthOfImage;
            detailHeightRate = ih / heightOfImage;

            Log.w("Values", detailStartX + "," + detailStartY + "," + width + "," + height);
            Log.w("Values", sX + "," + sY + "," + iw + "," + ih);

            Toast.makeText(this, "เลือกส่วนข้อมูลเรียบร้อยแล้ว", Toast.LENGTH_LONG).show();
            setCropped();
        }
    }

    void setCropped() {
        imageView.setCropped(true);
        cropStartBtn.setEnabled(false);
        cropStartBtn.setImageResource(R.mipmap.default_confirm_icon);
        reCropBtn.setEnabled(true);
        reCropBtn.setImageResource(R.mipmap.redo_icon);
        nextBtn.setEnabled(true);
        nextBtn.setImageResource(R.mipmap.arrow_next_icon);
    }

    @OnClick(R.id.reCropBtn)
    void reCrop() {
        imageView.setImageBitmap(photo);
        imageView.setCropped(false);
        cropStartBtn.setEnabled(true);
        cropStartBtn.setImageResource(R.mipmap.confirm_icon);
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
        Intent intent = new Intent(this, CropIDActivity.class);
        if (imageUri != null && imagePath != null) {
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
