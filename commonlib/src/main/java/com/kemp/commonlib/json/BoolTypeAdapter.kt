package com.kemp.commonlib.json

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class BoolTypeAdapter(private val default: Boolean? = false) : TypeAdapter<Boolean?>() {

    override fun write(p0: JsonWriter?, p1: Boolean?) {
        p0?.value(p1)
    }

    override fun read(p0: JsonReader?): Boolean? {
        p0 ?: return default
        return when (p0.peek()) {
            JsonToken.NUMBER -> {
                return try {
                    p0.nextDouble() > 0
                } catch (e: Exception) {
                    SafeGsonBuilder.errorPrint(e)
                    default
                }

            }
            JsonToken.STRING -> {
                return try {
                    val b = p0.nextString()
                    try {
                        b.toDouble() > 0
                    } catch (e: Exception) {
                        b!!.toBoolean()
                    }
                } catch (e: Exception) {
                    SafeGsonBuilder.errorPrint(e)
                    default
                }
            }
            JsonToken.BOOLEAN -> {
                return try {
                    p0.nextBoolean()
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
}