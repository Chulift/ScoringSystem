package com.example.chulift.demoapplication.Answer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chulift.demoapplication.Class.ExamStorage;
import com.example.chulift.demoapplication.Class.Utilities;
import com.example.chulift.demoapplication.Config.Config;
import com.example.chulift.demoapplication.ExamStorage.ManageExamStorageActivity;
import com.example.chulift.demoapplication.Image.ImagePossessing;
import com.example.chulift.demoapplication.Login.LoginActivity;
import com.example.chulift.demoapplication.R;
import com.example.chulift.demoapplication.httpConnect.ConnectServer;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CaptureAnswerActivity extends AppCompatActivity {
    private final String url = Config.serverURL + "createAnswerFromImage.php";
    private final String imgPath = Config.serverImagePathURL;

    private static final int REQUEST_ACTION_CAMERA = 1;
    private Uri imageUri = null;
    private String imagePath = null;
    private ExamStorage examStorage;
    private int id_answer;
    private Bitmap photo;
    private Bitmap resultBitmap;
    @BindView(R.id.imageView3)ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_answer);
        ButterKnife.bind(this);
        String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("examStorage");
            examStorage = new Gson().fromJson(jsonMyObject, ExamStorage.class);
        }
        imageUri = Utilities.setupCamera(this);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_ACTION_CAMERA);
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
                imageView.setImageBitmap(bitmap);
                resultBitmap = bitmap;
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
    }

    void dump(Bitmap bitmap) {
        File file = Utilities.saveImageFromBitmap(bitmap);
        Uri resultImg;
        if (file != null) {
            resultImg = Uri.fromFile(file);
            imageUri = resultImg;
            imagePath = file.getAbsolutePath();
        } else {
            //Toast.makeText(this, "No Image", Toast.LENGTH_LONG).show();
            Log.e("dump","no image");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ACTION_CAMERA && resultCode == RESULT_OK) {
            setBitmap();


        }
        Intent intent;
        if (id_answer != 0) {
            intent = new Intent(this, SelectAnswerActivity.class);
            intent.putExtra("previousPage", "CaptureAnswerActivity");
        } else {
            //Toast.makeText(this,"สร้างเฉลยผิดพลาด",Toast.LENGTH_SHORT).show();
            //intent = new Intent(this, ChooseAnswerMethodActivity.class);
        }
        //intent.putExtra("examStorage", new Gson().toJson(examStorage));
        //startActivity(intent);
    }


    private int uploadFile(String sourceFileUri) {
        int resp = 0;
        File sourceFile = null;
        try {
            sourceFile = new File(sourceFileUri);
        } catch (Exception e) {
            Log.e("CreateFile", "CreateFileFromSource:" + e.toString());
        }
        Log.i("File", "Value:" + sourceFile.getPath());
        if (!sourceFile.isFile()) {
            Log.e("CheckFile", "Source File not exist :" + imagePath);
        } else {
            String path = imgPath + sourceFile.getName();
            final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
            Log.i("id", examStorage.getId_examStorage() + "");
            Log.i("email", LoginActivity.getUser().getEmail() + "");
            Log.i("image_path", path + "");
            Log.i("max_score", examStorage.getNumScore());
            Log.i("uploaded_file", sourceFile.getName());
            RequestBody req = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("id_examStorage", examStorage.getId_examStorage() + "")
                    .addFormDataPart("user_email", LoginActivity.getUser().getEmail() + "")
                    .addFormDataPart("image_path", path + "")
                    .addFormDataPart("max_score", examStorage.getNumScore() + "")
                    .addFormDataPart("uploaded_file", sourceFile.getName(), RequestBody.create(MEDIA_TYPE_JPG, sourceFile)).build();
            Log.i("before start", "prepare");

            resp = ConnectServer.connectHttp(url, req);

//            id_answer = Integer.parseInt(response);
            //resp = response.code();
            /*String respID = null;
            try {
                resp = response.code();
                respID = response.body().string().toString();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("status","fail"+e.getMessage());
            }*/
            //id_answer = Integer.parseInt(respID);
            Log.e("id_answer", resp + "");
            if (resp == 200) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "อัพโหลดเฉลยเรียบร้อย", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                final int finalResp = resp;
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "ไม่สามารถอัพโหลดข้อมูลได้ กรุณาตรวจสอบการเชื่อมต่ออินเทอร์เน็ต " + finalResp, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        return resp;
    }

    public void run() {

    }
    @OnClick(R.id.button)void back(){
        Intent intent = new Intent(this,ManageExamStorageActivity.class);
        intent.putExtra("examStorage",new Gson().toJson(examStorage));
        startActivity(intent);
    }


    void upload() {
        if (imagePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("wait..");
            progressDialog.show();
            new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... voids) {
                    Log.d("PathOfImage", imagePath);


                    int r =  uploadFile(imagePath);

                    return r;
                }

                @Override
                protected void onPostExecute(Integer integer) {
                    Log.e("response", "" + integer);
                    progressDialog.dismiss();
                }
            }.execute();
        }
    }
}