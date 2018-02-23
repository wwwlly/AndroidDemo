package com.kemp.demo.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wangkp on 2018/2/5.
 */

public class MyButton extends AppCompatButton {

    private OnClickListener1 listener;

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick();
                }
            }
        });
    }

    public void setOnClickListener1(OnClickListener1 onClickListener) {
        this.listener = onClickListener;
    }

    public void setBtnText(String text) {
        setText(text);
    }

    public interface OnClickListener1 {
        void onClick();
    }
}
