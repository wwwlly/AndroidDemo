package com.kemp.demo;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.kemp.demo.launchmode.AActivity;
import com.kemp.demo.launchmode.LaunchModeActivity;

/**
 * activity的启动方式和启动模式
 * Created by wangkp on 2018/5/9.
 */

public class LaunchModeDemo extends LaunchModeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setButton(AActivity.class);
    }

    @Override
    public void onClick(View v) {
//        startActivityComponent();
        startActivityImpllicit();
    }

    private void startActivityComponent(){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.kemp.demo","com.kemp.demo.launchmode.AActivity"));
        startActivity(intent);
    }

    /**
     *
     * 隐式启动,在启动的时候是不明确的,需要匹配系统或AndroidManifest.xml中的intent-filter定义,只有action和category和data完全匹配时,才会启动.
     * 需在mainifest文件中对应的activity的intent-filter中添加<category android:name="android.intent.category.DEFAULT" />
     * android.intent.category.DEFAULT是一种默认的category,在startActivity时自动添加
     * https://developer.android.com/guide/topics/manifest/category-element
     */
    private void startActivityImpllicit(){
        Intent intent = new Intent();
        intent.setAction("com.kemp.demo.launchmode.action");
        intent.addCategory("com.kemp.demo.launchmode.AActivity");
        startActivity(intent);
    }
}
