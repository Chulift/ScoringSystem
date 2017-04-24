package com.example.chulift.demoapplication.answerSheet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chulift.demoapplication.classes.ExamStorage;
import com.example.chulift.demoapplication.classes.Utilities;
import com.example.chulift.demoapplication.config.Config;
import com.example.chulift.demoapplication.image.ImagePossessing;
import com.example.chulift.demoapplication.R;
import com.example.chulift.demoapplication.httpConnect.ConnectServer;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CaptureAnswerSheetActivity extends Activity {

    private final String url = Config.projectUrl + "uploadAnswerSheet.php";
    private final String imgPath = Config.serverImagePathURL;

    private static final int REQUEST_ACTION_CAMERA = 1;
    private Bitmap photo;
    private String imagePath = null;
    private Uri imageUri = null;
    private ExamStorage examStorage;
    @BindView(R.id.imageViewAnswer)
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_answer_sheet);
        ButterKnife.bind(this);
        String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //jsonMyObject = extras.getString("myObject");
            jsonMyObject = extras.getString("examStorage");
            examStorage = new Gson().fromJson(jsonMyObject, ExamStorage.class);
        }
        imageUri = Utilities.setupCamera(this);
        Log.d("imageUri", imageUri.toString());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_ACTION_CAMERA);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AnswerSheetListActivity.class);
        intent.putExtra("examStorage", new Gson().toJson(examStorage));
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ACTION_CAMERA && resultCode == RESULT_OK) {
            setBitmap();
        } else if (resultCode == RESULT_CANCELED) {
            this.onBackPressed();
        }
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
                }
                return temp2;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                //imgView.setImageBitmap(bitmap);
                dump(bitmap);
                try {
                    imagePath = Utilities.getRealPathFromURI(getApplicationContext(), imageUri);
                    Log.e("onResult", "Got path:" + imagePath);
                } catch (Exception e) {
                    Log.e("onResult", "Can't get Path.");
                }
                upload();
            }
        }.execute();
        Intent intent = new Intent(CaptureAnswerSheetActivity.this, AnswerSheetListActivity.class);
        intent.putExtra("examStorage", new Gson().toJson(examStorage));
        startActivity(intent);
        finish();
    }

    void dump(Bitmap bitmap) {
        File file = Utilities.saveImageFromBitmap(bitmap);
        Uri resultImg;
        if (file != null) {
            resultImg = Uri.fromFile(file);
            imageUri = resultImg;
            imagePath = file.getAbsolutePath();
        } else {
            Log.e("dump", "no image");
        }
    }


    private int uploadFile(String sourceFileUri) {
        int resp = 0;
        File sourceFile = null;
        try {
            sourceFile = new File(sourceFileUri);
        } catch (Exception e) {
            Log.e("CreateFile", "CreateFileFromSource:" + e.toString());
        }
        Log.i("File", "Value:" + (sourceFile != null ? sourceFile.getPath() : null));
        if (!sourceFile.isFile()) {
            Log.e("CheckFile", "Source File not exist :" + imagePath);
        } else {
            String path = examStorage.getStoragePath();
            final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
            RequestBody req = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("id_examStorage", examStorage.getId_examStorage())
                    .addFormDataPart("user_email", examStorage.getUser_email())
                    .addFormDataPart("status", "not yet")
                    .addFormDataPart("image_path", path)
                    .addFormDataPart("uploaded_file", sourceFile.getName(), RequestBody.create(MEDIA_TYPE_JPG, sourceFile)).build();
            resp = ConnectServer.connectHttp(url, req);
        }
        return resp;
    }


    void upload() {
        if (imagePath != null) {

            new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... voids) {
                    Log.d("PathOfImage", imagePath);
                    return uploadFile(imagePath);
                }

                @Override
                protected void onPostExecute(Integer resp) {
                    Log.e("response", "" + resp);
                    if (resp == 200) {
                        Toast.makeText(getApplicationContext(), "อัพโหลดข้อมูลเทมเพลทเรียบร้อย", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "ไม่สามารถอัพโหลดข้อมูลได้ กรุณาตรวจสอบการเชื่อมต่ออินเทอร์เน็ต " + resp, Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
    }


}
