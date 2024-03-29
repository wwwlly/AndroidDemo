package com.kemp.demo.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.kemp.demo.R;

import java.io.IOException;

/**
 * Created by wangkp on 2018/3/19.
 */

public class CameraDemo extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PictureCallback {

    public static final String TAG = CameraDemo.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS = 1;

    private SurfaceView surfaceView;

    private Camera camera;
    private CameraManager manager;

    private boolean hasSurface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        hasSurface = false;
        surfaceView = findViewById(R.id.surface_view);
        Button btn = findViewById(R.id.btn_capture);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture();
            }
        });
    }

    private void startResult(byte[] data) {
        ImageView iv = findViewById(R.id.iv);
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        if (bitmap == null) {
            Log.d(TAG, "bitmap is null");
            return;
        }
        Log.d(TAG, "bitmap w:" + bitmap.getWidth() + ", h:" + bitmap.getHeight());
        iv.setVisibility(View.VISIBLE);
        iv.setImageBitmap(bitmap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (hasSurface) {
            initCamera(surfaceView.getHolder());
        } else {
            surfaceView.getHolder().addCallback(this);
        }
    }

    @Override
    protected void onPause() {
        camera.release();
        super.onPause();
        Log.d(TAG, "onPause");

        if (!hasSurface) {
            surfaceView.getHolder().removeCallback(this);
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
        if (!hasSurface) {
            hasSurface = true;
            initCamera(surfaceView.getHolder());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        hasSurface = false;
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSIONS);
            return;
        }
        camera = Camera.open();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(0, cameraInfo);

        Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        parameters.setRotation(270);
        parameters.setPreviewSize(1920, 1080);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    private void capture() {
//        camera.setOneShotPreviewCallback(new Camera.PreviewCallback() {
//            @Override
//            public void onPreviewFrame(byte[] data, Camera camera) {
//
//            }
//        });
        camera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        startResult(data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0) {
            int cameraPermission = grantResults[0];
            if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
                initCamera(surfaceView.getHolder());
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                        REQUEST_PERMISSIONS);
                finish();
            }
        }
    }
}
