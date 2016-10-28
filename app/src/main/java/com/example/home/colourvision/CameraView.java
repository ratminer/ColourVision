package com.example.home.colourvision;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Mat;

/**
 * Created by robertfernandes on 10/28/2016.
 */

public class CameraView extends JavaCameraView {

    private int color;

    private int searchRadius = 4;

    float width;
    float height;

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        color = Color.BLACK;

    }

    @Override
    protected void drawScreen(Canvas canvas) {
        drawFocusBox(canvas);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

        double[][] colours = new double[searchRadius*searchRadius][4];


        return inputFrame.rgba();
    }

    private void drawFocusBox(Canvas canvas) {
        Paint p = new Paint();

        float w = canvas.getWidth()/mScale;
        float h = canvas.getHeight()/mScale;

        width = w;
        height = h;

        float size = w/10;

        float radius = size/6;

        p.setStyle(Paint.Style.FILL);
        p.setColor(color);

        canvas.drawCircle(w/2, h/2, radius, p);

        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.WHITE);
        p.setStrokeWidth(5);

        canvas.drawRect(w/2-size, h/2-size, w/2+size, h/2+size, p);

        canvas.drawCircle(w/2, h/2, radius, p);
    }

    public float getCanvasWidth(){
        return width;
    }

    public float getCanvasHeight(){
        return height;
    }


}
