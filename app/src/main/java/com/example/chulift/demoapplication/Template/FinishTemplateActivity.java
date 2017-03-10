package com.example.chulift.demoapplication.Template;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chulift.demoapplication.Class.Utilities;
import com.example.chulift.demoapplication.Config.Config;
import com.example.chulift.demoapplication.Class.Template;
import com.example.chulift.demoapplication.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FinishTemplateActivity extends AppCompatActivity {
    private final String TAG = "FinishActivity";
    private final String url = Config.serverURL + "GetTemplateInfo.php";
    private ProgressDialog progressDialog;
    private Target loadTarget;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user)
    TextView user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_template);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String template_name = extras.getString("template_name");
            Log.i("name", template_name);
            String postbody = "{\"" + "template_name\"" + ":\"" + template_name + "\"}";
            progressDialog = new ProgressDialog(FinishTemplateActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("กรุณารอสักครู่...");
            progressDialog.show();
            FinishTemplateActivity.AsyncTaskGetData taskGetUser = new FinishTemplateActivity.AsyncTaskGetData(url, postbody);
            taskGetUser.execute();
        }
        Utilities.setToolbar(this);
    }

    @OnClick(R.id.okBtn)
    void home() {
        startActivity(new Intent(getApplicationContext(), ShowTemplateListActivity.class));
        finish();
    }

    public class AsyncTaskGetData extends AsyncTask<Object, Object, JSONObject> {
        String Url, postBody;
        JSONObject result = null;

        public AsyncTaskGetData(String url, String postBody) {
            this.Url = url;
            this.postBody = postBody;
        }

        @Override
        protected JSONObject doInBackground(Object... params) {
            try {
                final MediaType Json = MediaType.parse("application/json; charset=utf-8");
                Request.Builder builder = new Request.Builder();

                Request request = builder
                        .url(Url)
                        .post(RequestBody.create(Json, postBody))
                        .build();

                OkHttpClient client = new OkHttpClient();

                Response response = client.newCall(request).execute();

                Log.d("Status of sever", response.toString());
                try {
                    result = new JSONObject(response.body().string());
                } catch (Exception e) {
                    Log.e(TAG, "Json Error: " + e.getLocalizedMessage());
                    return null;
                }
                Log.d("Value", result.getString("id_template"));
            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e(TAG, "Error: " + e.getLocalizedMessage());
                return null;
            } catch (Exception e) {
                Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
                return null;
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Template template = new Template(result);
            if (template.getDataIsset()) {
                float AS_startX = 0, AS_startY = 0, AS_width = 0, AS_height = 0,
                        CS_startX = 0, CS_startY = 0, CS_width = 0, CS_height = 0,
                        IS_startX = 0, IS_startY = 0, IS_width = 0, IS_height = 0;
                //cal rect
                //Answer session
                try {
                    AS_startX = Float.parseFloat(template.getStartXRate());
                    AS_startY = Float.parseFloat(template.getStartYRate());
                    AS_width = Float.parseFloat(template.getWidthRate());
                    AS_height = Float.parseFloat(template.getHeightRate());
                } catch (Exception e) {
                    Log.i("Convert Error", "AS Session");
                }
                //Code session
                try {
                    CS_startX = Float.parseFloat(template.getStartXRateCode());
                    CS_startY = Float.parseFloat(template.getStartYRateCode());
                    CS_width = Float.parseFloat(template.getWidthRateCode());
                    CS_height = Float.parseFloat(template.getHeightRateCode());
                } catch (Exception e) {
                    Log.i("Convert Error", "CS Session");
                }
                //Information session
                try {
                    IS_startX = Float.parseFloat(template.getStartXRateInfo());
                    IS_startY = Float.parseFloat(template.getStartYRateInfo());
                    IS_width = Float.parseFloat(template.getWidthRateInfo());
                    IS_height = Float.parseFloat(template.getHeightRateInfo());
                } catch (Exception e) {
                    Log.i("Convert Error", "IS Session");
                }
                //create rect
                Bitmap bitmap = null;
                final float finalAS_startX = AS_startX;
                final float finalAS_startY = AS_startY;
                final float finalAS_width = AS_width;
                final float finalAS_height = AS_height;
                Log.w("value as", finalAS_startX + "," + finalAS_startY + "," + finalAS_width + "," + finalAS_height);
                final float finalCS_startX = CS_startX;
                final float finalCS_startY = CS_startY;
                final float finalCS_width = CS_width;
                final float finalCS_height = CS_height;
                final float finalIS_startX = IS_startX;
                final float finalIS_startY = IS_startY;
                final float finalIS_width = IS_width;
                final float finalIS_height = IS_height;
                loadTarget = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        try {
                            bitmap = Bitmap.createBitmap(bitmap);
                        } catch (Exception e) {
                            Log.e("Create bitmap Error", e.toString());
                        }
                        try {
                            Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                            Canvas canvas = new Canvas(mutableBitmap);
                            Paint paint = new Paint();
                            paint.setColor(Color.RED);
                            paint.setAntiAlias(true);
                            paint.setStrokeWidth(5);
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeJoin(Paint.Join.ROUND);
                            paint.setStrokeCap(Paint.Cap.ROUND);

                            imageView.setImageBitmap(mutableBitmap);

                            int imageView_width = mutableBitmap.getWidth();
                            int imageView_height = mutableBitmap.getHeight();

                            Log.w("w/h", imageView_width + "," + imageView_height);
                            float startX = finalAS_startX * imageView_width;
                            float startY = finalAS_startY * imageView_height;
                            float endX = startX + (finalAS_width * imageView_width);
                            float endY = startY + (finalAS_height * imageView_height);

                            Log.w("value", startX + "," + startY + "," + endX + "," + endY);
                            canvas.drawRect(startX, startY, endX, endY, paint);
                            paint.setColor(Color.GREEN);
                            startX = finalCS_startX * imageView_width;
                            startY = finalCS_startY * imageView_height;
                            endX = startX + (finalCS_width * imageView_width);
                            endY = startY + (finalCS_height * imageView_height);

                            canvas.drawRect(startX, startY, endX, endY, paint);
                            paint.setColor(Color.BLUE);

                            startX = finalIS_startX * imageView_width;
                            startY = finalIS_startY * imageView_height;
                            endX = startX + (finalIS_width * imageView_width);
                            endY = startY + (finalIS_height * imageView_height);

                            canvas.drawRect(startX, startY, endX, endY, paint);

                        } catch (Exception e) {
                            Log.e("Draw Rect", e.toString());
                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Log.e("onBitmapFailed", "Error to download image.");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };
                Picasso.with(getApplicationContext()).load(template.getTemplate_path()).into(loadTarget);
            } else {
                Log.i("Data Error", "Don't have template");
            }
            //finish
            progressDialog.dismiss();
        }
    }
}