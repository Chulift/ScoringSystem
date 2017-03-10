package com.example.chulift.demoapplication.Class;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.chulift.demoapplication.Login.LoginActivity;
import com.example.chulift.demoapplication.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by chulift on 3/6/2017.
 */

public class Utilities {

    public static void hideInputSoftKeyboard(Context context) {
        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static File saveImageFromBitmap(Bitmap bitmap) {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(dir, getNewFileName());
        FileOutputStream fOut;
        if (bitmap != null) {
            try {
                fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();
                fOut.close();
            } catch (Exception e) {
                Log.e("file Error(CreateTemp)", e.toString());
            }
            return file;
        } else {
            return null;
        }
    }

    public static Uri setupCamera(Context context) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");

        Uri imageUri = context.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        return imageUri;
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String output = cursor.getString(idx);
        cursor.close();
        return output;
    }

    public static String getNewFileName() {
        int day, month, year;
        int second, minute, hour;
        GregorianCalendar date = new GregorianCalendar();

        day = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH);
        year = date.get(Calendar.YEAR);

        second = date.get(Calendar.SECOND);
        minute = date.get(Calendar.MINUTE);
        hour = date.get(Calendar.HOUR);

        String t_name = (hour + "" + minute + "" + second + "" + day + "" + (month + 1) + "" + year);
        String tag = t_name + ".jpg";
        return tag;
    }

    public static void setToolbar(Context context) {
        Toolbar toolbar = (Toolbar) ((Activity) context).findViewById(R.id.toolbar);
        TextView user = (TextView) ((Activity) context).findViewById(R.id.user);
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ((AppCompatActivity) context).getSupportActionBar().setDisplayShowTitleEnabled(false);
        user.setText(user.getText() + LoginActivity.getUser().getName());
    }

    public static void setStrictMode() {
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
}
