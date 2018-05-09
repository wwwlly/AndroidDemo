package com.kemp.demo.launchmode;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by wangkp on 2018/5/9.
 */

public class BActivity extends LaunchModeActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setButton(CActivity.class);
    }
}
