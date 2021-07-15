package com.kemp.demo.activity

import android.os.Bundle
import com.kemp.annotations.Description
import com.kemp.demo.arithmetic.TestSort
import com.kemp.demo.base.BaseActivity

@Description("测试")
class TestDemo : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TestSort.test()
    }
}