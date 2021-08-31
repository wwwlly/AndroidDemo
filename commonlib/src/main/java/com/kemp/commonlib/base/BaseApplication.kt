package com.kemp.commonlib.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 * Created by zhaolp on 2019/1/3.
 */
abstract class BaseApplication : Application() {

    companion object {
        private lateinit var mApp: BaseApplication
        val instance: BaseApplication
            get() = mApp
    }

    var mActivity: Activity? = null
    var isShumeiInit = false

    override fun onCreate() {
        super.onCreate()
        mApp = this
//        SPUtils.Builder().setContext(applicationContext).setPrefsName(AppConstants.SP_NAME).build()
//        ARouter.openLog()
//        ARouter.openDebug()
//        ARouter.init(this)
        initTinker()

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
                mActivity = activity
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            }
        })
    }


    private fun initTinker() {
//        var isArgee = SPUtils.getBoolean(SPConstants.SP_INIT_ARGEE, false)
//        if (isArgee) {
//            Bugly.setIsDevelopmentDevice(this, false)
//            Bugly.init(this, "a1bc69bbfa", false)
//        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
//        MultiDex.install(this)
//        Beta.installTinker()
    }

    override fun startActivity(intent: Intent?, options: Bundle?) {
        super.startActivity(intent, options)
//        mActivity?.exitTransition()
    }

}