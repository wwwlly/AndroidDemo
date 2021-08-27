package com.kemp.commonlib.json

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.TypeAdapter
import com.google.gson.internal.Streams
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class StrTypeAdapter(private val transformer: String.() -> String = { this }) : TypeAdapter<String>() {
    private val default = ""
    override fun write(p0: JsonWriter?, p1: String?) {
        p0?.value(p1)
    }

    override fun read(p0: JsonReader?): String {
        p0 ?: return default
        return try {
            val o = Streams.parse(p0)
            if (o is JsonObject || o is JsonArray) {
                o.toString()
            } else {
                o.asString.transformer()
            }
        } catch (e: Exception) {
            default
        }
    }
}