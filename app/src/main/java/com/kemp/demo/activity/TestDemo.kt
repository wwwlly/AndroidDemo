package com.kemp.demo.activity

import android.os.Bundle
import com.kemp.annotations.Description
import com.kemp.commonlib.tools.invoke
import com.kemp.demo.arithmetic.TestSort
import com.kemp.demo.base.BaseActivity
import com.kemp.demo.base.ShowTextActivity
import com.kemp.demo.utils.DebugLog
import com.kemp.demo.utils.TestTime
import com.kemp.demo.utils.TimeUtils

@Description("测试")
class TestDemo : ShowTextActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        TestSort.test()
//        TestTime.testCurrentTimeMillis()
//        TestTime.getTimeZoneInfo()
//        testTime()
        testCommonLib()
    }

    private fun testTime() {
        setText("testTime")
        val currentTime = System.currentTimeMillis()
        appendText("currentTime: $currentTime")
        appendText("currentTime: ${TimeUtils.format(currentTime)}")
    }

    private fun testCommonLib() {
        val str: String = true("true")("false")
        DebugLog.d("str: $str")
    }
}