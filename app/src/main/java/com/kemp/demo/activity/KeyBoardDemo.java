package com.kemp.demo.activity;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.kemp.demo.R;
import com.kemp.demo.utils.KeyBoardUtil;

public class KeyBoardDemo extends AppCompatActivity {

    private EditText editText;
    private KeyboardView keyboardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        editText = findViewById(R.id.edit_text);
        keyboardView = findViewById(R.id.keyboard_view);

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(editText.hasFocus()){
                    //用来初始化我们的软键盘
                    new KeyBoardUtil(keyboardView, editText).showKeyboard();
                }
                return true;
            }
        });
    }
}
