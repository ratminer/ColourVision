package com.example.home.colourvision;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.opencv.android.JavaCameraView;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends Activity implements CvCameraViewListener2 {

    private static final String TAG = "OCVSample::Activity";

    private CameraView mOpenCvCameraView;

    private int height;
    private int width;

    int searchRadius = 5;

    private SurfaceHolder holder;
    private View view;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    public MainActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //view = (View)findViewById(R.id.focus_box);
        //view.setVisibility(View.VISIBLE);

        setContentView(R.layout.show_camera);
        mOpenCvCameraView = (CameraView) findViewById(R.id.show_camera_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    public void onPause() {
        super.onPause();
        if(mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
    public void onResume() {
        super.onResume();
        if(!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if(mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        this.height = height;
        this.width = width;
    }

    @Override
    public void onCameraViewStopped() {

    }

    double[][] colours = new double[searchRadius*searchRadius][4];

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

        //return mOpenCvCameraView.onCameraFrame(inputFrame);

        for(int i = 0; i < searchRadius-1; i++) {
            for(int j = 0; j < searchRadius-1; j++) {
                colours[i+j] = inputFrame.rgba().get(i,j);
            }
        }

        int r = (int)inputFrame.rgba().get(width/2, height/2)[0];
        int g = (int)inputFrame.rgba().get(width/2, height/2)[1];
        int b = (int)inputFrame.rgba().get(width/2, height/2)[2];
        int a = (int)inputFrame.rgba().get(width/2, height/2)[3];

        mOpenCvCameraView.setColor(Color.argb(a,r,g,b));

        return inputFrame.rgba();

    }

    // averages colour over certain area
    // finds most popular colour
    private int getColor(double[][] data) {

        int count = 0;

        double red = 0;
        double blue = 0;
        double green = 0;

        for(double[] d : data) {
            count++;
            red += d[0];
            blue += d[1];
            green += d[3];
        }

        red = red/count;
        blue = blue/count;
        green = green/count;

        return Color.rgb((int)red,(int)blue,(int)green);
    }
}