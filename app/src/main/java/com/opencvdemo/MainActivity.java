package com.opencvdemo;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2
{

    public static String TAG = "MainActivity";
    JavaCameraView javaCameraView;
    Mat mRgba, imgGray, imgCanny;


    static{



    }

    BaseLoaderCallback mBaseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {

            switch (status) {
                case BaseLoaderCallback.SUCCESS:
                        javaCameraView.enableView();
                    break;
                    default:
                        super.onManagerConnected(status);

                        break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        javaCameraView = (JavaCameraView) findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (javaCameraView!=null){
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (javaCameraView!=null){
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (OpenCVLoader.initDebug()){
            Log.d(TAG, "loaded");
            mBaseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }else{
            Log.d(TAG, "Not noted");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mBaseLoaderCallback);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(width, height, CvType.CV_8SC4);
        imgGray = new Mat(width, height, CvType.CV_8SC1);
        imgCanny = new Mat(width, height, CvType.CV_8SC1);

    }

    @Override
    public void onCameraViewStopped() {
            mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();

        Imgproc.cvtColor(mRgba, imgGray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(mRgba, imgCanny, 50, 150);
        return imgCanny;
    }


}
