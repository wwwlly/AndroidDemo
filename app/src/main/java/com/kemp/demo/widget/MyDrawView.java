package com.kemp.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wangkp on 2018/4/10.
 */

public class MyDrawView extends View {

    private Path path;
    private Paint paint;

    public MyDrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        path = new Path();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(20);
        //设置线冒
        paint.setStrokeCap(Paint.Cap.ROUND);
        //设置着色器 渐变色
        LinearGradient lg = new LinearGradient(0,0,100,100,Color.RED,Color.BLUE, Shader.TileMode.MIRROR);
        SweepGradient sg = new SweepGradient(0,0,Color.RED,Color.BLUE);
        paint.setShader(lg);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //等腰三角形
        path.moveTo(200, 100);
        path.lineTo(300, 300);
        path.lineTo(100, 300);
        path.close();
        canvas.drawPath(path, paint);

//        canvas.drawCircle(100, 100, 50, paint);

//        canvas.drawLines();
    }
}
