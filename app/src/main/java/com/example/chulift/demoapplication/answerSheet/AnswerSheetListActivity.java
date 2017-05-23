package com.example.chulift.demoapplication.answerSheet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.chulift.demoapplication.CameraImageActivity;
import com.example.chulift.demoapplication.MenusActivity;
import com.example.chulift.demoapplication.adapter.AnswerSheetAdapter;
import com.example.chulift.demoapplication.classes.AnswerSheetCamera;
import com.example.chulift.demoapplication.classes.ExamStorage;
import com.example.chulift.demoapplication.classes.ConvertJSONString;
import com.example.chulift.demoapplication.classes.Utilities;
import com.example.chulift.demoapplication.config.Config;
import com.example.chulift.demoapplication.examStorage.ManageExamStorageActivity;
import com.example.chulift.demoapplication.R;
import com.example.chulift.demoapplication.httpConnect.ConnectServer;
import com.example.chulift.demoapplication.image.ImagePossessing;
import com.example.chulift.demoapplication.login.LoginActivity;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AnswerSheetListActivity extends AppCompatActivity {

    private final String url = Config.serverUrl + Config.projectName + "model/answersheet/getAnswerSheet.php";
    private final String uploadAnswerSheerUrl = Config.serverUrl + Config.projectName + "model/answersheet/uploadAnswerSheet.php";

    private final int REQUEST_ACTION_CAMERA = 1;
    private Uri imageUri = null;

    RecyclerView answerSheetRecyclerView;
    private ArrayList arrayList;
    private String resp;
    private ProgressDialog progressDialog;
    private ExamStorage examStorage;
    private AnswerSheetAdapter adapter;
    SharedPreferences sharedPreferences;
    AnswerSheetCamera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_sheet_list);
        String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("examStorage");
            examStorage = new Gson().fromJson(jsonMyObject, ExamStorage.class);
        } else Log.e("Error bundle", "Extras = null");
        ButterKnife.bind(this);
        answerSheetRecyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        initial();
        Utilities.setToolbar(this);
        sharedPreferences = getSharedPreferences("CONFIG", MODE_PRIVATE);
        String camera = sharedPreferences.getString("answerSheetCamera", AnswerSheetCamera.CAMERA_1.name());
        mCamera = AnswerSheetCamera.valueOf(camera);
        System.out.println(mCamera.name());
    }

    private void initial() {
        progressDialog.show();
        new AsyncTask<Void, Void, ArrayList>() {

            @Override
            protected ArrayList doInBackground(Void... params) {
                try {
                    resp = new ConnectServer().getJSONString(url + "?id=" + examStorage.getExamStorageID());
                } catch (NullPointerException e) {
                    Toast.makeText(getApplicationContext(), "เกิดข้อผิดพลาดขึ้น!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MenusActivity.class);
                    startActivity(intent);
                }
                Log.d("RESP", resp);
                arrayList = new ArrayList<>();
                arrayList = ConvertJSONString.getAnswerSheetArray(resp);
                return arrayList;
            }

            @Override
            protected void onPostExecute(ArrayList arrayList) {
                Log.d("POST", arrayList.size() + "");
                try {
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                    answerSheetRecyclerView.setHasFixedSize(true);
                    answerSheetRecyclerView.setLayoutManager(layoutManager);
                    adapter = new AnswerSheetAdapter(arrayList);
                    answerSheetRecyclerView.setAdapter(adapter);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        this.back();
    }

    @OnClick(R.id.back_btn)
    void back() {
        Intent intent = new Intent(getApplicationContext(), ManageExamStorageActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.add_answer_sheet_btn)
    void addAnswerSheetBtn() {
        if (mCamera == AnswerSheetCamera.CAMERA_1) {
            imageUri = Utilities.setupCamera(this);
            Log.d("imageUri", imageUri.toString());
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_ACTION_CAMERA);
        }
        else if (mCamera == AnswerSheetCamera.CAMERA_2) {
            addAnswerSheetBtn2();
        }
    }

    private void addAnswerSheetBtn2() {
        Intent intent = new Intent(this, CameraImageActivity.class);
        intent.putExtra("examStorage", new Gson().toJson(examStorage));
        startActivity(intent);
    }

    @OnClick(R.id.test_camera)
    void testCamera() {
        Intent intent = new Intent(AnswerSheetListActivity.this, TestCameraActivity.class);
        intent.putExtra("examStorage", new Gson().toJson(examStorage));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ACTION_CAMERA && resultCode == RESULT_OK) {
            new UploadAnswerSheetImage(imageUri).execute();
        }
    }

    private class UploadAnswerSheetImage extends AsyncTask<Void, Void, Bitmap> {

        private Bitmap photo;
        private String imagePath = null;
        private Uri imageUri = null;

        UploadAnswerSheetImage(Uri imageUri) {
            this.imageUri = imageUri;
            Log.i("imageUri", String.valueOf(imageUri));
        }

        private void dump(Bitmap bitmap) {
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


        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap temp2 = null;
            try {
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                System.out.println(photo.getWidth() + "*" + photo.getHeight());
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
            super.onPostExecute(bitmap);
            dump(bitmap);
            try {
                imagePath = Utilities.getRealPathFromURI(AnswerSheetListActivity.this, imageUri);
                Log.e("onResult", "Got path:" + imagePath);
            } catch (Exception e) {
                Log.e("onResult", "Can't get Path.::" + e.toString());
            }
            upload();
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
                String path = examStorage.getExamStoragePath();
                final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
                RequestBody req = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("id_examStorage", examStorage.getExamStorageID())
                        .addFormDataPart("user_email", LoginActivity.getUser().getEmail())
                        .addFormDataPart("status", "not yet")
                        .addFormDataPart("image_path", path)
                        .addFormDataPart("uploaded_file", sourceFile.getName(), RequestBody.create(MEDIA_TYPE_JPG, sourceFile)).build();

                resp = ConnectServer.connectHttp(uploadAnswerSheerUrl, req);
            }
            return resp;
        }

        private void upload() {
            if (imagePath != null) {
                new AsyncTask<Void, Void, Integer>(){

                    @Override
                    protected Integer doInBackground(Void... voids) {
                        return uploadFile(imagePath);
                    }

                    @Override
                    protected void onPostExecute(Integer integer) {
                        super.onPostExecute(integer);
                        final int resp = integer;
                        Log.e("response", "" + resp);
                        if (resp <= 200) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "อัพโหลดเรียบร้อย", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (resp == 500) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "เกิดข้อผิดพลาดในการตรวจ กรุณาตรวจกระดาษคำตอบ แล้วถ่ายอีกครั้ง", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "ไม่สามารถอัพโหลดข้อมูลได้ กรุณาตรวจสอบการเชื่อมต่ออินเทอร์เน็ต (error: " + resp + ")", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }.execute();
            }
        }
    }
}
