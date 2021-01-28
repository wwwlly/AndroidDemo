package com.kemp.demo.activity;

import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kemp.demo.base.BaseActivity;

/**
 * 生命周期
 */
public class LifecycleActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLifecycle().addObserver((GenericLifecycleObserver) (source, event) -> log("onStateChanged: event = " + event));
    }
}
