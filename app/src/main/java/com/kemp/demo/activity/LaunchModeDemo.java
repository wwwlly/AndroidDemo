package com.kemp.demo.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kemp.demo.launchmode.AActivity;
import com.kemp.demo.launchmode.LaunchModeActivity;

/**
 * activity启动方式：显示启动、隐式启动
 *
 * activity启动模式:
 * standard 标准模式
 * 每次启动一个Activity都会又一次创建一个新的实例入栈，无论这个实例是否存在。
 * singleTop 栈顶复用模式
 * 分两种处理情况：须要创建的Activity已经处于栈顶时，此时会直接复用栈顶的Activity。不会再创建新的Activity；
 * 若须要创建的Activity不处于栈顶，此时会又一次创建一个新的Activity入栈，同Standard模式一样。
 * singleTask 栈内复用模式
 * 若须要创建的Activity已经处于栈中时，此时不会创建新的Activity，而是将存在栈中的Activity上面的其他Activity所有销毁，使它成为栈顶。
 * singleInstance 单实例模式
 * SingleInstance比較特殊，是全局单例模式，是一种加强的SingleTask模式。它除了具有它所有特性外，还加强了一点：具有此模式的Activity仅仅能单独位于一个任务栈中。
 * 参考：https://blog.csdn.net/elisonx/article/details/80397519
 *
 * activity的生命周期
 * Created by wangkp on 2018/5/9.
 */

public class LaunchModeDemo extends LaunchModeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setButton(AActivity.class);
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
