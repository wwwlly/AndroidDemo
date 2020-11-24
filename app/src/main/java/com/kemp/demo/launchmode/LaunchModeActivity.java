package com.kemp.demo.launchmode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * standard
 * singleTop
 * singleTask
 * singleInstance
 *
 * Created by wangkp on 2018/5/9.
 */

public abstract class LaunchModeActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate taskid:" + getTaskId());
        super.onCreate(savedInstanceState);
        button = new Button(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(lp);
        setContentView(button);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.d(TAG, "onCreate with PersistableBundle");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent");
        super.onNewIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    /**
     * @param cls
     * @param onClickListener
     * @attr ref android.R.styleable#TextView_textAllCaps 设置Button字母大小写
     * {@link android.widget.TextView#setAllCaps(boolean)} 设置Button字母大小写
     */
    public void setButton(final Class<? extends Activity> cls, View.OnClickListener onClickListener) {
        if (cls == null) {
            return;
        }
        button.setAllCaps(false);
        button.setText("start_" + cls.getSimpleName());
        button.setOnClickListener(onClickListener);
    }

    public void setButton(final Class<? extends Activity> cls) {
        setButton(cls, 0);
    }

    /**
     * @param cls
     * @param flags {@link Intent#FLAG_ACTIVITY_SINGLE_TOP}
     */
    public void setButton(final Class<?> cls, int flags) {
        button.setAllCaps(false);
        button.setText("start_" + cls.getSimpleName());
        button.setOnClickListener((v) -> {
            Intent intent = new Intent(this, cls);
            if (flags != 0)
                intent.setFlags(flags);
            startActivity(intent);
        });
    }
}
