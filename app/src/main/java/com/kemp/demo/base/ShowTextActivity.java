package com.kemp.demo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.kemp.demo.R;

/**
 * 显示文本的activity
 */
public abstract class ShowTextActivity extends AppCompatActivity {

    private TextView tvContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_text);

        tvContent = findViewById(R.id.tv_content);
    }

    protected void setText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        tvContent.setText(text);
    }

    protected void appendText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        tvContent.append("\n");
        tvContent.append(text);
    }
}
