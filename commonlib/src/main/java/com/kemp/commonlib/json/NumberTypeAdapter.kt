package com.kemp.commonlib.json

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

open class NumberTypeAdapter<T : Number>(private val default: T?) : TypeAdapter<Number?>() {

    var tClass: Class<T> = Double::class.javaObjectType as Class<T>

    companion object {
        inline fun <reified N : Number> create(default: N?): NumberTypeAdapter<N> {
            val typeAdapter = NumberTypeAdapter(default)
            typeAdapter.tClass = N::class.java
            return typeAdapter
        }
    }

    override fun write(p0: JsonWriter?, p1: Number?) {
        p0?.value(p1)
    }

    override fun read(p0: JsonReader?): Number? {
        p0 ?: return default
        return when (p0.peek()) {
            JsonToken.NUMBER, JsonToken.STRING -> {
                return try {
                    transformValue(p0.nextString())
                } catch (e: Exception) {
                    SafeGsonBuilder.errorPrint(e)
                    default
                }
            }
            JsonToken.BOOLEAN -> {
                return try {
                    transformValue(p0.nextBoolean())
                } catch (e: Exception) {
                    SafeGsonBuilder.errorPrint(e)
                    default
                }
            }
            else -> {
                p0.skipValue()
                default
            }
        }
    }

    private fun transformValue(value: Any?): T? {
        value ?: return tClass.cast(default)
        return tClass.cast(when (value) {
            is String -> when (tClass) {
                Int::class.java, Int::class.javaObjectType -> value.toInt()
                Float::class.java, Float::class.javaObjectType -> value.toFloat()
                Double::class.java, Double::class.javaObjectType -> value.toDouble()
                else -> default
            }
            is Boolean -> tClass.cast(if (value) 1 else 0)
            else -> default
        })
    }
}