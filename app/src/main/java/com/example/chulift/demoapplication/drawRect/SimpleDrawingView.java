package com.example.chulift.demoapplication.drawRect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class SimpleDrawingView extends android.support.v7.widget.AppCompatImageView {
    private int paintColor = Color.RED;

    public void setPaintColor(int paintColor) {
        this.paintColor = paintColor;
        drawPaint.setColor(paintColor);
    }

    private Paint drawPaint;
    float pointX = 0;
    float pointY = 0;
    float startX = 0;
    float startY = 0;
    Bitmap bm;
    private Boolean isCropped = false;

    float bmWidth, bmHeight;

    public SimpleDrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }

    public SimpleDrawingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }

    public SimpleDrawingView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }

    private void setupPaint() {
// Setup paint with color and stroke styles
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public Paint getDrawPaint() {
        return drawPaint;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        this.bm = bm;
        bmWidth = bm.getWidth();
        bmHeight = bm.getHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isCropped) {
            pointX = event.getX();
            pointY = event.getY();

        }
// Checks for the event that occurs
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isCropped) {

                    startX = pointX;
                    startY = pointY;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            default:
                return false;
        }
// Force a view to draw again
        postInvalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (bm != null) {
            canvas.drawBitmap(bm, null, new Rect(0, 0, this.getWidth(), this.getHeight()), null);
        }
        canvas.drawRect(startX, startY, pointX, pointY, drawPaint);
    }

    public void setCropped(Boolean cropped) {
        isCropped = cropped;
    }

    public float getStartY() {
        return startY;
    }

    public float getStartX() {
        return startX;
    }

    public float getPointX() {
        return pointX;
    }

    public float getPointY() {
        return pointY;
    }

    public float getW() {
        return pointX - startX;
    }

    public float getH() {
        return pointY - startY;
    }


}
