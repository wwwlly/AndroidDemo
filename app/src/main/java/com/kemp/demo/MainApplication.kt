package com.kemp.demo

import android.support.multidex.MultiDexApplication
import com.kemp.commonlib.util.SPUtils

class MainApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        SPUtils.Builder().setContext(this).build()
    }
}