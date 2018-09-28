package com.kemp.demo.activity;

import android.graphics.BitmapRegionDecoder;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

//import com.davemorrissey.labs.subscaleview.ImageSource;
//import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.kemp.demo.R;
//import com.kemp.demo.utils.GlideApp;

/**
 * 巨图地址
 * https://upload.wikimedia.org/wikipedia/commons/3/33/Physical_Political_World_Map.jpg
 */
public class BitmapDemo extends AppCompatActivity {

    private static final String photo = "https://upload.wikimedia.org/wikipedia/commons/3/33/Physical_Political_World_Map.jpg";

//    BitmapRegionDecoder bitmapRegionDecoder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);

//        SubsamplingScaleImageView imageView = findViewById(R.id.imageView);
//        ImageView imageView1 = findViewById(R.id.imageView1);

//        imageView.setImage(ImageSource.asset("Physical_Political_World_Map.jpg"));
//        imageView.setScaleAndCenter(1.0f, new PointF(0, 0));

//        GlideApp.with(this).load(photo).into(imageView1);
    }
}
