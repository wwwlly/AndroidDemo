package com.kemp.demo.utils

import java.util.*

object TestTime {

    /**
     * System.currentTimeMillis()获取的是系统时区的当前时间戳而不是格林尼治时区时间戳
     */
    fun testCurrentTimeMillis() {
        val currentTime = System.currentTimeMillis()
        DebugLog.d("currentTime: $currentTime ${TimeUtils.format(currentTime)}")
        DebugLog.d("getTomorrow0: ${getTomorrow0(currentTime)} ${TimeUtils.format(getTomorrow0(currentTime))}")
        DebugLog.d("2021-08-26 00:00:00: ${TimeUtils.parse("2021-08-26 00:00:00")}")
        DebugLog.d("2021-08-27 00:00:00: ${TimeUtils.parse("2021-08-27 00:00:00")}")
    }

    /**
     * 获取明天凌晨的时间戳 28800000 中国东八区+8个小时
     *
     * dateTime-(dateTime + TimeZone.getDefault().getRawOffset())%(1000*3600*24)
     */
    private fun getTomorrow0(dateTime: Long) = dateTime - (dateTime + 28800000) % 86400000 + 86400000

    fun getTimeZoneInfo() {
//        val zoneIds = TimeZone.getAvailableIDs()
//        zoneIds.forEach {
//            DebugLog.d(it)
//        }

        val default = TimeZone.getDefault()
        DebugLog.d("default zone name : ${default.displayName}")
        DebugLog.d("default zone rawOffset : ${default.rawOffset}")

        TimeZone.setDefault(TimeZone.getTimeZone("GTM+8"))
        DebugLog.d("GTM+8 currentTime ${System.currentTimeMillis()}")
        TimeZone.setDefault(TimeZone.getTimeZone("GTM+6"))
        DebugLog.d("GTM+6 currentTime ${System.currentTimeMillis()}")
        TimeZone.setDefault(TimeZone.getTimeZone("GTM-8"))
        DebugLog.d("GTM-8 currentTime ${System.currentTimeMillis()}")
    }
}