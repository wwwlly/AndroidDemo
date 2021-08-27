package com.kemp.demo.utils

import java.text.SimpleDateFormat

object TimeUtils {

    private const val DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"

    fun format(time: Long, pattern: String = DEFAULT_DATE_PATTERN): String = SimpleDateFormat(pattern).format(time)

    fun parse(time: String, pattern: String = DEFAULT_DATE_PATTERN) = SimpleDateFormat(pattern).parse(time).time
}