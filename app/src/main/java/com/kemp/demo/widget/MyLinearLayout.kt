package com.kemp.demo.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import com.kemp.demo.activity.ViewEventDemo
import com.kemp.demo.utils.DebugLog

class MyLinearLayout : LinearLayout {

    private var lastX = 0
    private var lastY = 0

    private var windowManager: WindowManager? = null
    private var layoutParams: WindowManager.LayoutParams? = null

    companion object {
        const val TAG = "MyLinearLayout"
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

        layoutParams = getFLayoutParams()
        windowManager = (context as Activity).windowManager
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        DebugLog.d(ViewEventDemo.TAG, "$TAG dispatchTouchEvent")
        val result = super.dispatchTouchEvent(ev)
        DebugLog.d(ViewEventDemo.TAG, "$TAG dispatchTouchEvent $result")
        return result
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        DebugLog.d(ViewEventDemo.TAG, "$TAG onInterceptTouchEvent")
//        val result = super.onInterceptTouchEvent(ev)
//        DebugLog.d(ViewEventDemo.TAG, "$TAG onInterceptTouchEvent $result")
//        return result
        return when (ev.action) {
            MotionEvent.ACTION_MOVE -> {
                lastX = ev.rawX.toInt()
                lastY = ev.rawY.toInt()
                true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_DOWN, MotionEvent.ACTION_CANCEL -> false
            else -> false
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        DebugLog.d(ViewEventDemo.TAG, "$TAG onTouchEvent")
//        val result = super.onTouchEvent(ev)
//        DebugLog.d(ViewEventDemo.TAG, "$TAG onTouchEvent $result")
//        return result

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = ev.rawX.toInt()
                lastY = ev.rawY.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val nowX = ev.rawX.toInt()
                val nowY = ev.rawY.toInt()
                val movedX = nowX - lastX
                val movedY = nowY - lastY
                lastX = nowX
                lastY = nowY
                layoutParams?.apply {
                    x += movedX
                    y += movedY
                }
                //更新悬浮球控件位置
                windowManager?.updateViewLayout(this, layoutParams)
                return true
            }
            else -> {

            }
        }
        return super.onTouchEvent(ev)
    }

    private fun getFLayoutParams() = WindowManager.LayoutParams().apply {
        //设置大小 自适应
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
    }
}