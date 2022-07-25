package com.kemp.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kemp.demo.R;
import com.kemp.demo.utils.DebugLog;
import com.kemp.demo.widget.TouchView1;
import com.kemp.demo.widget.TouchView2;

/**
 * 事件分发机制
 */
public class ViewTouchDemo extends AppCompatActivity {

    public static final String TAG = ViewTouchDemo.class.getSimpleName();

    private TouchView1 touchView1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_touch);

        touchView1 = findViewById(R.id.touch_view);
        ViewGroup touchView2 = findViewById(R.id.touch_view2);
        touchView2.requestDisallowInterceptTouchEvent(true);
        touchView2.setOnClickListener(view -> {
            DebugLog.d(TAG, "子view clicked");
        });
//        addContent();
    }

    private void addContent() {
        for (int i = 0; i < 5; i++) {
            TouchView2 linearLayout = new TouchView2(this);
            TextView textView = new TextView(this);
            TouchView1.LayoutParams layoutParams = new TouchView1.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 300);
            textView.setLayoutParams(layoutParams);
            textView.setText("经常用一天工作，却用三天等审批" + i);
            linearLayout.addView(textView);
            touchView1.addView(linearLayout);
            linearLayout.setOnClickListener(view -> {
                DebugLog.d(TAG, "子view clicked");
            });

//            linearLayout.requestDisallowInterceptTouchEvent(true);
        }
    }
}
