package com.kemp.demo.widget

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.MotionEvent
import com.kemp.demo.activity.ViewEventDemo
import com.kemp.demo.utils.DebugLog

class MyTextView : AppCompatTextView {

    companion object {
        const val TAG = "MyTextView"
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        DebugLog.d(ViewEventDemo.TAG, "$TAG init")
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        DebugLog.d(ViewEventDemo.TAG, "$TAG dispatchTouchEvent")
        val result = super.dispatchTouchEvent(ev)
        DebugLog.d(ViewEventDemo.TAG, "$TAG dispatchTouchEvent $result")
        return result
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        DebugLog.d(ViewEventDemo.TAG, "$TAG onTouchEvent")
        val result = super.onTouchEvent(ev)
        DebugLog.d(ViewEventDemo.TAG, "$TAG onTouchEvent $result")
        return result
    }
}