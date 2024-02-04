package com.kemp.demo.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Process


object ToolUtils {


    //获取当前进程名
    fun getCurrentProcessName(context: Context): String {

        try {
            val activityThread =
                ReflectUtil.invokeMethod("android.app.ActivityThread", "currentActivityThread")
            return ReflectUtil.invokeMethod(activityThread, "getProcessName") as String
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        DebugLog.d("ToolUtils", "getCurrentProcessName pid")
        val pid = Process.myPid()
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = manager.runningAppProcesses
        if (manager == null || runningAppProcesses == null) {
            return ""
        }

        for (processInfo in runningAppProcesses) {
            if (processInfo.pid == pid) {
                return processInfo.processName
            }
        }
        return ""
    }
}