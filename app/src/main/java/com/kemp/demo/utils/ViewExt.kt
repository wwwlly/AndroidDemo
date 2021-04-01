package com.kemp.demo.utils

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.*
import android.support.annotation.*
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.*

/**
 * 改变color的透明度
 * @param f 取值范围 0.0~1.0
 * 100% FF / 95% F2 / 90% E6 / 85% D9 / 80% CC / 75% BF / 70% B3 / 65% A6 / 60% 99 / 55% 8C /
 *  50% 80 / 45% 73 / 40% 66 / 35% 59 / 30% 4D / 25% 40 / 20% 33 / 15% 26 / 10% 1A / 5% 0D / 0% 00
 */
inline infix fun Int.alpha(f: Float): Int {
    val a = if (f < 0) 0f else if (f > 1) 1f else f
    return this and 0xffffff or ((255 * a).toInt() shl 24)
}

inline fun View?.gone() = Unit.apply { this@gone?.visibility = View.GONE }
inline fun View?.visible() = Unit.apply { this@visible?.visibility = View.VISIBLE }
inline fun View?.inVisible() = Unit.apply { this@inVisible?.visibility = View.INVISIBLE }
inline fun View?.toggleVisibility(visibility: Boolean? = true) {
    when (visibility) {
        null -> this@toggleVisibility.inVisible()
        true -> this@toggleVisibility.visible()
        false -> this@toggleVisibility.gone()
    }
}

fun TextView.setDrawableRight(drawable: Drawable, bounds: Rect = Rect(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)) {
    drawable.bounds = bounds
    setCompoundDrawables(null, null, drawable, null)
}

var View.leftMargin: Int
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin ?: 0
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin = value
    }

var View.topMargin: Int
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin ?: 0
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = value
    }

var View.rightMargin: Int
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin ?: 0
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin = value
    }

var View.bottomMargin: Int
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin ?: 0
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin = value
    }

inline fun Bitmap.toDrawable() = BitmapDrawable(this)

inline fun Drawable.toBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, if (opacity !== PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565)
    val canvas = Canvas(bitmap)
    setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    draw(canvas)
    return bitmap
}

inline fun AttributeSet?.obtainStyle(context: Context?, @StyleableRes attrs: IntArray, @AttrRes defStyleAttr: Int = 0, @StyleRes defStyleRes: Int = 0, l: TypedArray.() -> Unit) {
    val typedArray = context?.obtainStyledAttributes(this, attrs, defStyleAttr, defStyleRes)
    typedArray?.l()
    typedArray?.recycle()
}

inline fun Drawable.setTintColor(@ColorInt color: Int) {
    val drawable = DrawableCompat.wrap(this).mutate()
    DrawableCompat.setTint(drawable, color)
}

abstract class StateKt {
    val STATE_ENABLED = android.R.attr.state_enabled
    val STATE_PRESSED = android.R.attr.state_pressed
    val STATE_CHECKED = android.R.attr.state_checked
    val STATE_CHECKABLE = android.R.attr.state_checkable
    val STATE_FOCUSED = android.R.attr.state_focused
    val STATE_SELECTED = android.R.attr.state_selected

    val STATE_UNENABLED = -STATE_ENABLED
    val STATE_UNPRESSED = -STATE_PRESSED
    val STATE_UNCHECKED = -STATE_CHECKED
    val STATE_UNCHECKABLE = -STATE_CHECKABLE
    val STATE_UNFOCUSED = -STATE_FOCUSED
    val STATE_UNSELECTED = -STATE_SELECTED

    val STATE_DEFAULT = 0

    infix fun <A> A.state(i: Int): Pair<A, IntArray> {
        if (i == STATE_DEFAULT) {
            return this to intArrayOf()
        }
        return this to intArrayOf(i)
    }

    infix fun <A> Pair<A, IntArray>.and(i: Int): Pair<A, IntArray> {
        if (i == STATE_DEFAULT) {
            return first to intArrayOf(*second)
        }
        return first to intArrayOf(*second, i)
    }

}

class MyStateListDrawable : StateListDrawable() {

    private var onStateChange: (() -> Unit)? = null

    fun onStateChange(l: () -> Unit): MyStateListDrawable {
        onStateChange = l
        return this
    }

    override fun onStateChange(stateSet: IntArray?): Boolean {
        return super.onStateChange(stateSet).apply {
            if (this) {
                onStateChange?.invoke()
            }
        }
    }
}

inline fun Activity.entryTransition(bool: Boolean = false) {
//    if(bool){
//        overridePendingTransition(R.anim.slide_in_bottom_to_up, R.anim.slide_out_hold)
//    }
}

inline fun Activity.exitTransition(bool: Boolean = false) {
//    if(bool){
//        overridePendingTransition(0, R.anim.slide_out_up_to_bottom)
//    }
}

fun TextView.fitLine(maxLine: Int = 1, minSize: Float = 10f, maxDeep: Int = 100) {
    if (maxLine > 0 && textSize > TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, minSize, resources.displayMetrics) && maxDeep > 0) {
        post {
            if (layout?.lineCount ?: -1 > maxLine) {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize - 1)
                fitLine(maxLine, minSize, maxDeep - 1)
            }
        }
    }
}

fun ConstraintLayout.change(l: ConstraintSet.() -> Unit) {
    val constraintSet = ConstraintSet()
    constraintSet.clone(this)
    constraintSet.l()
    constraintSet.applyTo(this)
}