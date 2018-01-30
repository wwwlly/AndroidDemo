package com.kemp.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.kemp.annotations.CustomAnnotation;
import com.kemp.compiler.HelloWorld;

/**
 * Created by wangkp on 2018/1/29.
 */

public class ProcesserDemo extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        TextView textView = findViewById(R.id.textView);
        textView.setText(testProcesser());
    }

    @CustomAnnotation("testProcesser")
    private String testProcesser(){
        return HelloWorld.test();
    }
}
