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

    int width=0;
    int height=0;

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

        for(int i = 0; i < searchRadius-1; i++) {
            for(int j = 0; j < searchRadius-1; j++) {
                colours[i+j] = inputFrame.rgba().get(i,j);
            }
        }

        int r = (int)inputFrame.rgba().get(width, height)[0];
        int g = (int)inputFrame.rgba().get(width, height)[1];
        int b = (int)inputFrame.rgba().get(width, height)[2];
        int a = (int)inputFrame.rgba().get(width, height)[3];

        setColor(Color.argb(a,r,g,b));

        return inputFrame.rgba();
    }

    private void drawFocusBox(Canvas canvas) {
        Paint p = new Paint();

        float width = canvas.getWidth()/mScale;
        float height = canvas.getHeight()/mScale;

        float size = width/10;

        float radius = size/6;

        p.setStyle(Paint.Style.FILL);
        p.setColor(color);

        canvas.drawCircle(width/2, height/2, radius, p);

        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.WHITE);
        p.setStrokeWidth(5);

        canvas.drawRect(width/2-size, height/2-size, width/2+size, height/2+size, p);
        canvas.drawCircle(width/2, height/2, radius, p);
    }
}
