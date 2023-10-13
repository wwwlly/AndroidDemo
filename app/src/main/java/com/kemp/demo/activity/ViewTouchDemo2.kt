package com.kemp.demo.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import com.kemp.demo.R
import com.kemp.demo.utils.DebugLog
import com.kemp.demo.widget.MyTextView

class ViewTouchDemo2 : AppCompatActivity() {

    private var floatRootView: View? = null//悬浮窗View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_touch2)
    }

    override fun onResume() {
        super.onResume()
        showWindow()
    }

    override fun onPause() {
        super.onPause()
        hideWindow()
    }

    private fun showWindow() {
        var layoutParam = WindowManager.LayoutParams().apply {
            //设置大小 自适应
            width = WRAP_CONTENT
            height = WRAP_CONTENT
            flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        }
        // 新建悬浮窗控件
        floatRootView = LayoutInflater.from(this).inflate(R.layout.layout_floating_view, null)
        val textView = floatRootView?.findViewById<MyTextView>(R.id.text_view)
        //设置拖动事件
//        floatRootView?.setOnTouchListener(ItemViewTouchListener(layoutParam, windowManager))
        textView?.setOnClickListener {
            DebugLog.d(ViewEventDemo.TAG, "textView setOnClickListener")
        }
        // 将悬浮窗控件添加到WindowManager
        windowManager.addView(floatRootView, layoutParam)
    }

    private fun hideWindow() {
        if (floatRootView != null && floatRootView?.windowToken != null && windowManager != null) {
            windowManager?.removeView(floatRootView)
        }
    }
}