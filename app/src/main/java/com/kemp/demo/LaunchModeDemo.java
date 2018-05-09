package com.kemp.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kemp.demo.launchmode.AActivity;
import com.kemp.demo.launchmode.LaunchModeActivity;

/**
 * Created by wangkp on 2018/5/9.
 */

public class LaunchModeDemo extends LaunchModeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setButton(AActivity.class);
    }
}
