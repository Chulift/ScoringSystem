package com.example.chulift.demoapplication.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


public class ImagePossessing {
    public static Bitmap totBlackWrite(Bitmap inGrayImage,int th){
        int height = inGrayImage.getHeight();
        int width = inGrayImage.getWidth();

        Bitmap tempBitmap = Bitmap.createBitmap(width,height,inGrayImage.getConfig());
        Log.w("ConvertBlackWrite","Converting");
        for(int x = 0 ;x <  width ;x++){
            for(int y = 0;y < height;y++)
            {
                int pixelImage = inGrayImage.getPixel(x,y);
                int R = Color.red(pixelImage);
                int A = Color.alpha(pixelImage);

                int grayColor = 0;
                if(R < th)
                    grayColor = 255;
                int newColor = Color.argb(A,grayColor,grayColor,grayColor);

                tempBitmap.setPixel(x,y,newColor);
            }
        }
        Log.w("ConvertBlackWrite","Finished");
        return tempBitmap;
    }
    public static Bitmap cutBackGroundImage(Bitmap bw,Bitmap original) {
        Log.w("cutBackGroundImage","Cutting");
        int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
        int height = bw.getHeight();
        int width = bw.getWidth();
        //start x
        for (int x = 0; x < width; x++) {
            int pixel = bw.getPixel(x, height / 2);
            if (Color.red(pixel) == 255) {
                x1 = x;
                break;
            }
        }
        //end x
        for (int x = width - 1; x >= 0; x--) {
            int pixel = bw.getPixel(x, height / 2);
            if (Color.red(pixel) == 255) {
                x2 = x;
                break;
            }
        }
        //start y
        for (int y = 0; y < height; y++) {
            int pixel = bw.getPixel((x2 - x1) / 2, y);
            if (Color.red(pixel) == 255) {
                y1 = y;
                break;
            }
        }
        //end y
        for (int y = height - 1; y >= 0; y--) {
            int pixel = bw.getPixel((x2 - x1) / 2, y);
            if (Color.red(pixel) == 255) {
                y2 = y;
                break;
            }
        }
        Log.w("cutBackGroundImage","Finished");
        //result
        if (x1 + x2 + y1 + y2 > 0) {
            return Bitmap.createBitmap(original, x1, y1, x2 - x1, y2 - y1);
        } else {
            return null;
        }
    }
    public static Bitmap toGrayScale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayScale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayScale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayScale;
    }
    public static Bitmap totBlackWrite(Bitmap bitmap){
        // first convert bitmap into OpenCV mat object
        Mat imageMat = new Mat (bitmap.getHeight(), bitmap.getWidth(),
                CvType.CV_8U, new Scalar(4));
        Bitmap myBitmap = bitmap.copy(Bitmap.Config.RGB_565, true);
        Utils.bitmapToMat(myBitmap, imageMat);

        // now convert to gray
        Mat grayMat = new Mat ( bitmap.getHeight(), bitmap.getWidth(),
                CvType.CV_8U, new Scalar(1));
        Imgproc.cvtColor(imageMat, grayMat, Imgproc.COLOR_RGB2GRAY, 1);

        // get the threshold image
        Mat thresholdMat = new Mat ( bitmap.getHeight(), bitmap.getWidth(),
                CvType.CV_8U, new Scalar(1));
        Imgproc.threshold(grayMat, thresholdMat , 128, 255, Imgproc.THRESH_BINARY);

        // convert back to bitmap for displaying
        Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.RGB_565);
        thresholdMat.convertTo(thresholdMat, CvType.CV_8UC1);
        Utils.matToBitmap(thresholdMat, resultBitmap);
        return  resultBitmap;
    }
}
