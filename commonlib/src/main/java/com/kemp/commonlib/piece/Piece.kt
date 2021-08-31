package com.kemp.commonlib.piece

import android.support.v4.util.LruCache
import com.google.gson.annotations.SerializedName
import com.google.gson.internal.`$Gson$Types`
import com.google.gson.reflect.TypeToken
import com.kemp.commonlib.json.SafeGsonBuilder
import com.tencent.mmkv.MMKV

annotation class PieceName(val name: String)
annotation class Interceptor(val name: String)
typealias PieceField = SerializedName

object Piece {
    val mmkv = MMKV.mmkvWithID("piece")
    val gson = SafeGsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().disableInnerClassSerialization().safeCreate()
    const val PACKAGE = "com.kemp.demo.piece"
    val cache = LruCache<Class<*>, Any>(20)
    val targetClass = hashSetOf<String>()

    @JvmStatic
    inline fun <reified T> get() = get(T::class.java)

    @JvmStatic
    fun <T> get(clazz: Class<T>): T? {
        var instance = cache[clazz]
        if (instance != null && clazz.isInstance(instance)) {
            return instance as? T
        }
        val name = clazz.getAnnotation(PieceName::class.java)?.name ?: return null
        val value = mmkv.decodeString(name, "")
        instance = gson.fromJson(value, clazz)
        if (instance != null) {
            cache.put(clazz, instance)
        }
        return instance
    }

    @JvmStatic
    inline fun <reified T> getCollection(): T? {
        val type = object : TypeToken<T>() {}
        val key = if (Collection::class.java.isAssignableFrom(type.rawType)) {
            TypeToken.get(`$Gson$Types`.getCollectionElementType(type.type, type.rawType)).rawType
        } else {
            T::class.java
        }
        var instance = cache[key]
        if (instance != null && key.isInstance(instance)) {
            return instance as? T
        }
        instance = gson.fromJson<T>(getAsString(key), type.type)
        if (instance != null) {
            cache.put(key, instance)
        }
        return instance
    }

    @JvmStatic
    fun <T> getAsString(clazz: Class<T>): String {
        val name = clazz.getAnnotation(PieceName::class.java)?.name ?: return ""
        return mmkv.decodeString(name, "") ?: ""
    }

    fun <T> removeCache(key: Class<T>) {
        cache.remove(key)
    }
}