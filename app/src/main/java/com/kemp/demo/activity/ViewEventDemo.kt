package com.kemp.demo.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.kemp.annotations.Description
import com.kemp.demo.R

/**
 * 事件分发
 */
@Description("自定义view，事件分发等")
class ViewEventDemo : AppCompatActivity() {

    companion object {
        const val TAG = "ViewEventDemo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_event)
    }

    fun onClickBtn1(view: View) {
        startActivity(Intent(this, ViewTouchDemo::class.java))
    }

    fun onClickBtn2(view: View) {
        startActivity(Intent(this, ViewTouchDemo2::class.java))
    }

}