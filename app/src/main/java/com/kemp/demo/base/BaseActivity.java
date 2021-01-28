package com.kemp.demo.base;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class BaseActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    protected void log(String msg) {
        Log.d(TAG, msg);
    }
}
