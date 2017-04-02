package com.example.chulift.demoapplication.Template;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chulift.demoapplication.Class.Utilities;
import com.example.chulift.demoapplication.Config.Config;
import com.example.chulift.demoapplication.Config.NumberPickerConfig;
import com.example.chulift.demoapplication.Login.LoginActivity;
import com.example.chulift.demoapplication.R;
import com.example.chulift.demoapplication.httpConnect.ConnectServer;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FillFormActivity extends AppCompatActivity {

    private Uri imageUri = null;
    private String imagePath = null;
    private Bitmap photo;

    private float templateStartXRate, templateStartYRate, templateWidthRate, templateHeightRate,
            idStartXRate, idStartYRate, idWidthRate, idHeightRate,
            detailStartXRate, detailStartYRate, detailWidthRate, detailHeightRate;
    private final String url = Config.serverURL + "uploadTemplate.php";
    private final String imgPath = Config.serverImagePathURL;
    private String templateName;
    @BindView(R.id.num_student_code_picker)
    NumberPicker studentCodePicker;
    @BindView(R.id.column_picker)
    NumberPicker columnPicker;
    @BindView(R.id.section_picker)
    NumberPicker sectionPicker;
    @BindView(R.id.choice_picker)
    NumberPicker choicePicker;
    @BindView(R.id.input_name)
    EditText inputTemplateName;
    @BindView(R.id.image_upload)
    ImageView imageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user)
    TextView user;
    @BindView(R.id.btn_upload)
    Button uploadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_form);
        ButterKnife.bind(this);
        columnPicker.setMaxValue(NumberPickerConfig.MAX_COLUMN);
        columnPicker.setMinValue(NumberPickerConfig.MIN_COLUMN);
        sectionPicker.setMaxValue(NumberPickerConfig.MAX_SECTION);
        sectionPicker.setMinValue(NumberPickerConfig.MIN_SECTION);
        choicePicker.setMaxValue(NumberPickerConfig.MAX_CHOICE);
        choicePicker.setMinValue(NumberPickerConfig.MIN_CHOICE);
        studentCodePicker.setMinValue(NumberPickerConfig.MIN_STUDENT_CODE);
        studentCodePicker.setMaxValue(NumberPickerConfig.MAX_STUDENT_CODE);

        Bundle extras = getIntent().getExtras();
        if (columnPicker.getValue() == 0 || sectionPicker.getValue() == 0 || choicePicker.getValue() == 0)
            uploadBtn.setEnabled(false);
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

            detailStartXRate = (float) extras.get("detailStartXRate");
            detailStartYRate = (float) extras.get("detailStartYRate");
            detailWidthRate = (float) extras.get("detailWidthRate");
            detailHeightRate = (float) extras.get("detailHeightRate");
            setBitmap();
            Log.i("Extras", imageUri.toString() + ", " + imagePath);
        }
        columnPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                checkInput();
            }
        });
        sectionPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                checkInput();
            }
        });
        choicePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                checkInput();
            }
        });
        studentCodePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                checkInput();
            }
        });
        Utilities.setToolbar(this);
    }

    private void checkInput() {
        if (columnPicker.getValue() == 0 || sectionPicker.getValue() == 0 || choicePicker.getValue() == 0 || studentCodePicker.getValue() == 0)
            uploadBtn.setEnabled(false);
        else uploadBtn.setEnabled(true);
    }

    @OnClick(R.id.back_btn)
    void back() {
        Intent intent = new Intent(this, CropDetailActivity.class);
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
            startActivity(intent);
            finish();
        }

    }

    private void setBitmap() {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                try {
                    photo = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                } catch (IOException e) {
                    Log.e("setBitmap", e.toString());
                }
                return photo;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                Bitmap temp = Bitmap.createBitmap(bitmap);
                try {
                    if (bitmap.getHeight() > bitmap.getWidth()) {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(-90);
                        temp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    }
                } catch (Exception e) {
                    Log.e("Matrix", e.toString());
                } finally {
                    imageView.setImageBitmap(temp);
                }
            }
        }.execute();
    }

    private boolean validate() {
        if (inputTemplateName.getText().toString().trim().equals("")) {
            inputTemplateName.setError("กรุณาใส่ชื่อเทมเพลท");
            return false;
        }
        return true;
    }

    @OnClick(R.id.btn_upload)
    void upload() {
        if (imagePath != null) {
            if (validate()) {
                new AlertDialog.Builder(FillFormActivity.this)
                        .setTitle("Confirm upload image.")
                        .setMessage("ต้องการอัพโหลดเทมเพลทหรือไม่?").setNegativeButton("ยกเลิก", null)
                        .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final ProgressDialog progressDialog = new ProgressDialog(FillFormActivity.this,
                                        R.style.AppTheme_Dark_Dialog);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setCancelable(false);
                                progressDialog.setMessage("กำลังอัพโหลดข้อมูลเทมเพลท...");
                                progressDialog.show();

                                new AsyncTask<Void, Void, Integer>() {
                                    @Override
                                    protected Integer doInBackground(Void... voids) {
                                        Log.d("PathOfImage", imagePath);
                                        return uploadFile(imagePath);
                                    }

                                    @Override
                                    protected void onPostExecute(Integer integer) {
                                        progressDialog.cancel();
                                        Intent finishTemplate = new Intent(getApplicationContext(), FinishTemplateActivity.class);
                                        finishTemplate.putExtra("template_name", templateName);
                                        startActivity(finishTemplate);
                                        finish();
                                    }
                                }.execute();
                            }
                        }).show();
            }
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
        Log.i("File", "Value:" + sourceFile.getPath());
        if (!sourceFile.isFile()) {
            Log.e("CheckFile", "Source File not exist :" + imagePath);
        } else {
            templateName = sourceFile.getName();

            String path = imgPath + sourceFile.getName();
            final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
            RequestBody req = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("num_student_code", String.valueOf(studentCodePicker.getValue()))
                    .addFormDataPart("num_column", columnPicker.getValue() + "")
                    .addFormDataPart("num_section", sectionPicker.getValue() + "")
                    .addFormDataPart("num_choice", choicePicker.getValue() + "")
                    .addFormDataPart("user_id_user", LoginActivity.getUser().getEmail())
                    .addFormDataPart("input_template_name", inputTemplateName.getText().toString().trim())
                    .addFormDataPart("template_name", sourceFile.getName())
                    .addFormDataPart("templateStartXRate", templateStartXRate + "")
                    .addFormDataPart("templateStartYRate", templateStartYRate + "")
                    .addFormDataPart("templateWidthRate", templateWidthRate + "")
                    .addFormDataPart("templateHeightRate", templateHeightRate + "")
                    .addFormDataPart("idStartXRate", idStartXRate + "")
                    .addFormDataPart("idStartYRate", idStartYRate + "")
                    .addFormDataPart("idWidthRate", idWidthRate + "")
                    .addFormDataPart("idHeightRate", idHeightRate + "")
                    .addFormDataPart("detailStartXRate", detailStartXRate + "")
                    .addFormDataPart("detailStartYRate", detailStartYRate + "")
                    .addFormDataPart("detailWidthRate", detailWidthRate + "")
                    .addFormDataPart("detailHeightRate", detailHeightRate + "")
                    .addFormDataPart("template_path", path)
                    .addFormDataPart("uploaded_file", sourceFile.getName(), RequestBody.create(MEDIA_TYPE_JPG, sourceFile)).build();


            resp = ConnectServer.connectHttp(url, req);
            if (resp == 200) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "อัพโหลดข้อมูลเทมเพลทเรียบร้อย", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                final int finalResp = resp;
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "ไม่สามารถอัพโหลดข้อมูลได้ กรุณาตรวจสอบการเชื่อมต่ออินเทอร์เน็ต", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        return resp;
    }
}