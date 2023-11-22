package com.kemp.demo.activity

import android.os.Bundle
import com.kemp.demo.R
import com.kemp.demo.base.ShowTextActivity
import com.kemp.demo.utils.Tools

class BootSelfActivity: ShowTextActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_boot_self)

        appendText("packageName: ${packageName} \n")
        appendText("processName: ${Tools.getProcessName()} \n")
    }
}