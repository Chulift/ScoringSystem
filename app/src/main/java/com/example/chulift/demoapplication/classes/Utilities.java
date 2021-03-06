package com.example.chulift.demoapplication.classes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

import com.example.chulift.demoapplication.login.LoginActivity;
import com.example.chulift.demoapplication.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Utilities {

    public static void hideInputSoftKeyboard(Context context) {
        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static boolean isOverlap(float startX, float startY, float pointX, float pointY, float targetStartXRate, float targetStartYRate, float targetWidthRate, float targetHeightRate, int widthOfImage, int heightOfImage) {
        boolean result = false;
        float targetStartX = targetStartXRate * widthOfImage;
        float targetStartY = targetStartYRate * heightOfImage;
        float targetPointX = targetStartX + (targetWidthRate * widthOfImage);
        float targetPointY = targetStartY + (targetHeightRate * heightOfImage);
        //case left
        if (pointX > targetStartX && pointX < targetPointX) {
            if (pointY > targetStartY && pointY < targetPointY) {
                //case top
                result = true;
            } else if (startY > targetStartY && startY < targetPointY) {
                //case bottom
                result = true;
            }

        }
        //case right
        else if (startX > targetStartX && startX < targetPointX) {
            if (pointY > targetStartY && pointY < targetPointY) {
                //case top
                result = true;
            } else if (startY > targetStartY && startY < targetPointY) {
                //case bottom
                result = true;
            }
        }
        return result;
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
            } catch (IOException e) {

            } catch (OutOfMemoryError e) {

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
        assert cursor != null;
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
        //noinspection ConstantConditions
        ((AppCompatActivity) context).getSupportActionBar().setDisplayShowTitleEnabled(false);
        try {
            user.setText(user.getText() + LoginActivity.getUser().getName());
        } catch (NullPointerException e){
            Intent intent = new Intent(context,LoginActivity.class);
            context.startActivity(intent);
        } catch (RuntimeException e) {
            Intent intent = new Intent(context,LoginActivity.class);
            context.startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent(context,LoginActivity.class);
            context.startActivity(intent);
        }
    }

    public static void setStrictMode() {
        // Permission StrictMode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
