package com.kemp.demo.launchmode;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 场景：A->B->C
 * 没有特殊说明启动模式为standard
 * standard
 * singleTop 在栈顶是单一的
 * singleTask 该模式并不会新建任务栈，启动该模式的activity时，如果activity在栈顶效果和singleTop一致，
 * 不在栈顶时则将该activity上的所有activity全部弹出，正如其名在任务栈中时单一的
 *
 * singleInstance 该模式会新建task栈，每次启动该模式的activity都会新建在新的任务栈中
 * C为singleInstance模式，并且C启动B C->B
 * 因为B是标准模式当C启动B时，程序返回标准模式的任务栈此时标准模式的任务栈是A-B-B,C在独立的一个任务栈。
 * note：此时一直返回都不会出现C activity，除非start C
 *
 * Created by wangkp on 2018/5/9.
 */

public class CActivity extends LaunchModeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setButton(BActivity.class);
    }
}
