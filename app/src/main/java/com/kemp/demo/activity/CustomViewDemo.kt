package com.kemp.demo.activity

import android.os.Bundle
import com.kemp.annotations.Description
import com.kemp.demo.R
import com.kemp.demo.base.BaseActivity
import com.kemp.demo.widget.RandomTextView
import kotlinx.android.synthetic.main.activity_custom_view.*

@Description("自定义View")
class CustomViewDemo: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view)

        btnRandomTextView.setOnClickListener {
            testRandomTextView()
        }
    }

    private fun testRandomTextView(){
        rtv.text = "87*6,5.43"
        rtv.setSpeeds(RandomTextView.HIGH_FIRST)
//        rtv.setSpeeds(arrayOf(7,6))
        rtv.start()
    }
}