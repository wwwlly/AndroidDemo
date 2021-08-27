package com.kemp.commonlib.json

import java.lang.reflect.Field

interface NamingStrategy {
    fun translateFieldName(f: Field?): String?
    fun translateJsonName(f: String?): String?
}