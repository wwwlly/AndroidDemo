package com.kemp.demo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 在Bitmap上画画
 * 1.创建一个空白的Bitmap；
 * 2.用该Bitmap创建一个Canvas；
 * 3.在Canvas上画画；
 * 4.重写onDraw()，画Bitmap
 */
public class BitmapDrawView extends View {

    public static final String TAG = "BitmapDrawView";
//    public static final String text = "hello如果要把一个变量从主内存传输到工作内存，那就要顺序的执行read和load操作，如果要把一个变量从工作内存回写到主内存，就要顺序的执行store和write操作。";
    public static final String text = "hello";
    private Context mContext;
    private Bitmap mBitmap;
    private Paint bitmapPaint;

    public BitmapDrawView(Context context) {
        this(context, null);
    }

    public BitmapDrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        bitmapPaint = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged w: " + w + ", h: " + h);
        initBitmap(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) {
            Log.d(TAG, "bitmap is null");
            return;
        }

        canvas.drawBitmap(mBitmap, 0, 0, bitmapPaint);
    }

    private void initBitmap(int w, int h) {
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawBitmap();
    }

    private void drawBitmap() {
        Canvas canvas = new Canvas(mBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(80);

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        int top = 100;
        top = (int) (0 - fontMetrics.ascent);
        Log.d(TAG, "top: " + top);
        canvas.drawText(text, 0, top, paint);
    }

}
