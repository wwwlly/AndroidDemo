package com.kemp.commonlib.tools

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kemp.commonlib.constants.Const
import com.tencent.mmkv.MMKV
import java.io.Serializable
import java.math.BigDecimal
import java.util.*
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

/**
 * Created by yinzj on 2019/3/19.
 */

inline fun <reified T> mmkv(key: String?, noinline default: (() -> T)? = null, mmkvId: String? = null) = object : ReadWriteProperty<Any, T> {

    val mmkv = if (mmkvId.isNullOrEmpty()) MMKV.defaultMMKV() else MMKV.mmkvWithID(mmkvId)

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        val key = key ?: property.name
        return when (T::class.java) {
            Int::class.java -> mmkv.decodeInt(key, default?.invoke() as? Int ?: 0)
            Long::class.java -> mmkv.decodeLong(key, default?.invoke() as? Long ?: 0)
            Float::class.java -> mmkv.decodeFloat(key, default?.invoke() as? Float ?: 0f)
            Double::class.java -> mmkv.decodeDouble(key, default?.invoke() as? Double ?: 0.0)
            Boolean::class.java -> mmkv.decodeBool(key, default?.invoke() as? Boolean ?: false)
            String::class.java -> mmkv.decodeString(key) ?: default?.invoke()
            ByteArray::class.java -> mmkv.decodeBytes(key) ?: default?.invoke()
            Set::class.java -> {
                try {
                    mmkv.decodeStringSet(key) ?: default?.invoke()
                } catch (e: Exception) {
                    null
                }
            }
            else -> {
                null
            }
        } as T
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        val key = key ?: property.name
        value ?: return
        when (value) {
            is Int -> mmkv.encode(key, value)
            is Long -> mmkv.encode(key, value)
            is Float -> mmkv.encode(key, value)
            is Double -> mmkv.encode(key, value)
            is Boolean -> mmkv.encode(key, value)
            is String -> mmkv.encode(key, value)
            is ByteArray -> mmkv.encode(key, value)
            is Set<*> -> {
                try {
                    mmkv.encode(key, value as Set<String>)
                } catch (e: Exception) {
                }
            }
        }
    }
}

inline fun <T> simpleBind(def: T? = null, key: String? = null, bundleKey: String? = Const.Intent.BUNDLE) = bindBundle<T>(key, def, bundleKey)

inline fun <T> bindBundle(key: String? = null, def: T? = null, bundleKey: String? = null): ReadWriteProperty<Any, T> = object : ReadWriteProperty<Any, T> {

    var extra: T? = null
    var isInitializ = false

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (!isInitializ) {
            val k = key ?: property.name
            extra = when (thisRef) {
                is Activity -> thisRef.intent?.extras?.get(k) as? T
                        ?: thisRef.intent?.extras?.getBundle(bundleKey ?: k)?.get(k) as? T ?: def
                is Fragment -> thisRef.arguments?.get(k) as? T
                        ?: thisRef.arguments?.getBundle(bundleKey ?: k)?.get(k) as? T ?: def
                is android.app.Fragment -> thisRef.arguments?.get(k) as? T
                        ?: thisRef.arguments?.getBundle(bundleKey ?: k)?.get(k) as? T ?: def
                else -> def
            }
            isInitializ = true
        }
        return extra as T
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        extra = value
        isInitializ = true
    }
}

open class VarLazy<T>(private val initializer: () -> T) : ReadWriteProperty<Any, T> {

    private object UNINITIALIZED_VALUE

    protected var _value: Any? = UNINITIALIZED_VALUE

    protected val isInit get() = _value !== UNINITIALIZED_VALUE

    @CallSuper
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (!isInit) {
            _value = initializer()
        }
        return _value as T
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        _value = value
    }
}

/**
 * 只会赋值一次
 */
class Once<T>(private var value: T, private val callback: (() -> Unit)? = null) : ReadWriteProperty<Any, T> {

    private var isInit = false

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return value
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        if (!isInit) {
            isInit = true
            this.value = value
            callback?.invoke()
        }
    }
}

inline fun <T> List<T>.take(vararg indexs: Int): List<T> {
    return indexs.filter { it in 0 until size }.map(this::get).toList()
}

inline fun <T> List<T>.take(range: IntRange): List<T> {
    return range.filter { it in 0 until size }.map(this::get).toList()
}

inline fun IntRange.random() = toList()[Random().nextInt(this.count())]

interface Action : Serializable {
    fun action(info: ActionInfo? = null)
}

interface ActionInfo {
    companion object {
        val OK = object : ActionInfo {}
        val CANCEL = object : ActionInfo {}
    }
}
typealias TRFun<T, R> = T.() -> R
typealias TRFun1<T, P, R> = T.(P) -> R
typealias TRFun2<T, P1, P2, R> = T.(P1, P2) -> R
typealias TRFun3<T, P1, P2, P3, R> = T.(P1, P2, P3) -> R
typealias TRFun4<T, P1, P2, P3, P4, R> = T.(P1, P2, P3, P4) -> R
typealias TRFun5<T, P1, P2, P3, P4, P5, R> = T.(P1, P2, P3, P4, P5) -> R
typealias TFun<T> = T.() -> Unit
typealias TFun1<T, P> = T.(P) -> Unit
typealias TFun2<T, P1, P2> = T.(P1, P2) -> Unit
typealias TFun3<T, P1, P2, P3> = T.(P1, P2, P3) -> Unit
typealias TFun4<T, P1, P2, P3, P4> = T.(P1, P2, P3, P4) -> Unit
typealias TFun5<T, P1, P2, P3, P4, P5> = T.(P1, P2, P3, P4, P5) -> Unit
typealias RFun<R> = () -> R
typealias RFun1<P, R> = (P) -> R
typealias RFun2<P1, P2, R> = (P1, P2) -> R
typealias RFun3<P1, P2, P3, R> = (P1, P2, P3) -> R
typealias RFun4<P1, P2, P3, P4, R> = (P1, P2, P3, P4) -> R
typealias RFun5<P1, P2, P3, P4, P5, R> = (P1, P2, P3, P4, P5) -> R
typealias Fun = RFun<Unit>
typealias Fun1<P> = RFun1<P, Unit>
typealias Fun2<P1, P2> = RFun2<P1, P2, Unit>
typealias Fun3<P1, P2, P3> = RFun3<P1, P2, P3, Unit>
typealias Fun4<P1, P2, P3, P4> = RFun4<P1, P2, P3, P4, Unit>
typealias Fun5<P1, P2, P3, P4, P5> = RFun5<P1, P2, P3, P4, P5, Unit>


inline fun <T, R> withNotNull(receiver: T?, block: T.() -> R?): R? {
    return receiver?.block()
}


inline operator fun IntArray?.get(index: Int, default: Int): Int {
    return this?.getOrElse(index, { default }) ?: default
}

inline operator fun IntArray.get(range: IntRange): IntArray {
    return range.map(this::get).toIntArray()
}

inline operator fun FloatArray?.get(index: Int, default: Float): Float {
    return this?.getOrElse(index, { default }) ?: default
}

inline operator fun FloatArray.get(range: IntRange): FloatArray {
    return range.map(this::get).toFloatArray()
}

inline operator fun <reified T> Array<T>?.get(index: Int, default: T): T {
    return this?.getOrElse(index, { default }) ?: default
}

inline operator fun <reified T> Array<T>.get(range: IntRange): Array<T> {
    return range.map(this::get).toTypedArray()
}

inline operator fun <reified T> List<T>?.get(index: Int, default: T): T {
    return this?.getOrElse(index, { default }) ?: default
}

inline operator fun <reified T> List<T>.get(range: IntRange): List<T> {
    return range.map(this::get).toList()
}

inline fun String?.def(default: String = "", condition: String?.() -> Boolean = { this.isNullOrEmpty() }): String = if (condition()) default else (this
        ?: default)


inline fun <reified T : Activity> Context.startAct(extra: Bundle? = null, requestCode: Int? = null) {
    val intent = Intent(this, T::class.java)
    extra?.let { intent.putExtras(extra) }
    when {
        requestCode == null -> startActivity(intent)
        this is Activity -> startActivityForResult(intent, requestCode)
    }
}

inline fun <reified T> String?.asAny(): T? {
    return if (this == null) {
        null
    } else {
        try {
            Gson().fromJson<T>(this, object : TypeToken<T>() {}.type)
        } catch (e: Exception) {
            null
        }
    }
}

inline fun <reified T> String?.asList(): List<T>? {
    return if (this == null) {
        null
    } else {
        try {
            Gson().fromJson<List<T>>(this, object : TypeToken<List<T>>() {}.type)
        } catch (e: Exception) {
            null
        }
    }
}

inline fun <T> Delegates.property(mProperty: KProperty0<T>): ReadWriteProperty<Any?, T> {
    return object : ReadWriteProperty<Any?, T> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return mProperty.get()
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            if (mProperty is KMutableProperty0<T>) {
                mProperty.set(value)
            }
        }
    }
}

inline fun Fragment.waitView(fm: FragmentManager, tag: String? = null, crossinline l: (View) -> Unit) {
    fm.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fmm: FragmentManager?, f: Fragment?, v: View?, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fmm, f, v, savedInstanceState)
            if (v != null && this@waitView == f) {
                l.invoke(v)
                fm.unregisterFragmentLifecycleCallbacks(this)
            }
        }
    }, false)
    fm.beginTransaction().add(this, tag).commitNowAllowingStateLoss()
}

inline fun Double?.priceFormat(): String {
    return when {
        this == null -> "0"
        toInt() - this == 0.0 -> "${toInt()}"
        else -> toString()
    }
}

inline fun String?.countFormat(): String {
    this ?: return "0"
    try {
        val i = toInt()
        if (i >= 10000) {
            val d = i / 10000.0
            val bigDecimal = d.toBigDecimal()
            return "${bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).toDouble().priceFormat()}w"
        }

    } catch (e: Exception) {
    }
    return this
}

fun String?.ellipEnd(maxLen: Int): String {
    this ?: return ""
    var count = 0
    var endIndex = 0
    for (i in 0 until this.length) {
        val item = this[i]
        if (item.toInt() < 128) {
            count += 1
        } else {
            count += 2
        }
        if (maxLen == count || item.toInt() >= 128 && maxLen + 1 == count) {
            endIndex = i
        }
    }
    return if (count <= maxLen) {
        this
    } else {
        this.substring(0, endIndex) + "..."
    }
}

// 三元运算 true("true")("false")
operator fun <T> Boolean.invoke(yes: T): (no: T) -> T {
    return if (this) { _ -> yes } else { v -> v }
}