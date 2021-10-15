package com.kemp.demo.provider

import android.app.Activity
import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import com.kemp.demo.utils.DebugLog

class MyProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        DebugLog.d("onCreate()")
        if (context != null && context is Application) {
            (context as Application).registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                    DebugLog.d("onActivityCreated()")
                    DebugLog.d("activity name: ${activity?.javaClass?.simpleName ?: ""}")
                }

                override fun onActivityStarted(activity: Activity?) {
                    DebugLog.d("onActivityStarted()")
                }

                override fun onActivityResumed(activity: Activity?) {
                    DebugLog.d("onActivityResumed()")
                }

                override fun onActivityPaused(activity: Activity?) {
                    DebugLog.d("onActivityPaused()")
                }

                override fun onActivityStopped(activity: Activity?) {
                    DebugLog.d("onActivityStopped()")
                }

                override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
                    DebugLog.d("onActivitySaveInstanceState()")
                }

                override fun onActivityDestroyed(activity: Activity?) {
                    DebugLog.d("onActivityDestroyed()")
                }

            })
        }
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        DebugLog.d("query()")
        return null
    }

    override fun getType(uri: Uri): String? {
        DebugLog.d("getType()")
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        DebugLog.d("insert()")
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        DebugLog.d("delete()")
        return 0
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        DebugLog.d("update()")
        return 0
    }
}