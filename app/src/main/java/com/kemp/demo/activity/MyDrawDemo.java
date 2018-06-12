package com.kemp.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kemp.demo.R;

/**
 * Created by wangkp on 2018/4/10.
 */

public class MyDrawDemo extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    private Button btn;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_draw);

        btn = findViewById(R.id.btn);
        textView = findViewById(R.id.text_view);

        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
//        textView.scrollBy(-50, 0);
        Log.d(TAG,"scroll x:" + textView.getScrollX() + ",y:" + textView.getScrollY());
        textView.scrollTo(50, 0);
    }
}
