package com.kemp.demo.launchmode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by wangkp on 2018/5/9.
 */

public abstract class LaunchModeActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    private Button button;
    private Class<? extends Activity> aClass;

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
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent");
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    public void setButton(final Class<? extends Activity> cls) {
        if(cls == null){
            return;
        }
        aClass = cls;
        button.setText("start_" + cls.getSimpleName());
        button.setOnClickListener(this);
    }

    /**
     *
     * @param cls
     * @param flags Intent.FLAG_ACTIVITY_SINGLE_TOP
     */
    public void setButton(final Class<?> cls, int flags) {
        button.setText("start_" + cls.getSimpleName());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = newIntent(cls);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }

    public Intent newIntent(Class<?> cls) {
        return new Intent(this, cls);
    }

    public void startActivity(Class<?> cls) {
        startActivity(newIntent(cls));
    }

    @Override
    public void onClick(View v) {
        startActivity(aClass);
    }
}
