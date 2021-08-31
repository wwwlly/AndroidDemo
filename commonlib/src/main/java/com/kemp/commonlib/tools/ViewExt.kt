package com.kemp.commonlib.tools

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
import com.kemp.commonlib.base.BaseApplication
//import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
//import com.tencent.mm.opensdk.openapi.WXAPIFactory
//import com.yiche.price.commonlib.R
//import com.yiche.price.commonlib.base.BaseApplication
//import com.yiche.price.commonlib.constants.GlobalConstants
//import com.yiche.price.tool.constant.AppConstants
import org.jetbrains.anko.AnkoViewDslMarker
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.findOptional
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.verticalLayout
import java.util.*


/**view ext
 * Created by CiCi on 2019/2/27.
 */

inline val Number.dp: Int
    get() {
        val scale = BaseApplication.instance.resources.displayMetrics.density
        return (toFloat() * scale + 0.5f).toInt()
    }

inline val Number.dpf: Float
    get() {
        val scale = BaseApplication.instance.resources.displayMetrics.density
        return (toFloat() * scale + 0.5f).toFloat()
    }

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

inline fun getResourceType(res: Int) = BaseApplication.instance.resources.getResourceTypeName(res)

inline val Int.colorInt: Int
    get() {
        require("color" == getResourceType(this), { "$this 不是color类型的资源" })
        return colorRes(this)
    }

inline val Int.str: String
    get() {
        require("string" == getResourceType(this), { "$this 不是string类型的资源" })
        return BaseApplication.instance.resources.getString(this)
    }

inline val Int.drawable: Drawable?
    get() {
        require("drawable" == getResourceType(this), { "$this 不是drawable类型的资源" })
        return drawableRes(this)
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

inline fun colorRes(@ColorRes colorId: Int) = ContextCompat.getColor(BaseApplication.instance, colorId)
inline fun drawableRes(@DrawableRes drawableId: Int):Drawable? = ContextCompat.getDrawable(BaseApplication.instance, drawableId)

inline val Context.inflater: LayoutInflater get() = LayoutInflater.from(this)
inline fun Context.inflate(@LayoutRes layoutId: Int) = LayoutInflater.from(this).inflate(layoutId, (this as? Activity)?.findOptional(android.R.id.content), false)
inline fun Context.inflate(@LayoutRes layoutId: Int, parent: ViewGroup? = (this as? Activity)?.findOptional(android.R.id.content), attachToRoot: Boolean = false) = LayoutInflater.from(this).inflate(layoutId, parent, attachToRoot)

inline fun <reified T : View> Fragment.lazyView(id: Int): Lazy<T> {
    return lazy { find<T>(id) }

}

inline fun ImageView.setImageUrl(url: String?, @DrawableRes errorResId: Int = 0, @DrawableRes placeholderResId: Int = 0) {
    val options = RequestOptions().placeholder(placeholderResId).error(errorResId)
    Glide.with(BaseApplication.instance).load(url).apply(options).into(this)
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

inline fun Context.horizontalLayout(theme: Int = 0, init: (@AnkoViewDslMarker _LinearLayout).() -> Unit): LinearLayout {
    return verticalLayout(theme) {
        orientation = LinearLayout.HORIZONTAL
        init.invoke(this)
    }
}

inline fun ViewManager.horizontalLayout(theme: Int = 0, init: (@AnkoViewDslMarker _LinearLayout).() -> Unit): LinearLayout {
    return verticalLayout(theme) {
        orientation = LinearLayout.HORIZONTAL
        init.invoke(this)
    }
}

//inline fun Context.openWXMiniProgram(path: String = "", id: String = GlobalConstants.WXMINI_PROGRAM_ID) {
//    val api = WXAPIFactory.createWXAPI(this, AppConstants.WXAPP_ID)
//    if (api?.isWXAppInstalled == false) {
//        Toast.makeText(this, R.string.error_wx_not_install, Toast.LENGTH_SHORT).show()
//        return
//    }
//    val req = WXLaunchMiniProgram.Req()
//    req.userName = id                // 填小程序原始id
//    req.path = path                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
//    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE// 可选打开 开发版，体验版和正式版
//    api.sendReq(req)
//}

inline fun AttributeSet?.obtainStyle(context: Context?, @StyleableRes attrs: IntArray, @AttrRes defStyleAttr: Int = 0, @StyleRes defStyleRes: Int = 0, l: TypedArray.() -> Unit) {
    val typedArray = context?.obtainStyledAttributes(this, attrs, defStyleAttr, defStyleRes)
    typedArray?.l()
    typedArray?.recycle()
}

inline fun Drawable.setTintColor(@ColorInt color: Int) {
    val drawable = DrawableCompat.wrap(this).mutate()
    DrawableCompat.setTint(drawable, color)
}

/**
 * @param radii 大小为4， 分别为左上、右上、右下、左下的圆角半径
 */
inline fun shape(@ColorInt color: Int = Color.WHITE, @ColorInt strokeColor: Int = Color.TRANSPARENT, strokeWidth: Int = 0, radius: Float = 0f, radii: FloatArray? = null) = GradientDrawable().apply {
    setColor(color)
    if (radii != null) {
        cornerRadii = floatArrayOf(
                radii[0, 0f], radii[0, 0f],
                radii[1, 0f], radii[1, 0f],
                radii[2, 0f], radii[2, 0f],
                radii[3, 0f], radii[3, 0f])
    } else {
        cornerRadius = radius
    }
    if (strokeWidth > 0) {
        setStroke(strokeWidth, strokeColor)
    }
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

class ColorKt(private val useColorRes: Boolean) : StateKt() {
    val colorStateList: ArrayList<Pair<Int, IntArray>> = arrayListOf()
    var item: Pair<Int, IntArray>? = null
        set(value) {
            withNotNull(value) {
                if (useColorRes) {
                    colorStateList.add(colorRes(this.first) to this.second)
                } else {
                    colorStateList.add(this)
                }
            }
        }
}

/**
 *  @param useColorRes true:使用color资源
 *  @sample
 *  textView.setTextColor(colorList(true) {
 *      item = R.color.public_black_000019 to intArrayOf(STATE_CHECKABLE, STATE_ENABLED)
 *  })
 */
inline fun colorList(useColorRes: Boolean = false, b: ColorKt.() -> Unit): ColorStateList {
    val colorKt = ColorKt(useColorRes).apply(b)
    val colorList = colorKt.colorStateList.map {
        it.first
    }.toIntArray()
    val stateList = colorKt.colorStateList.map {
        it.second
    }.toTypedArray()
    return ColorStateList(stateList, colorList)
}

class SelectorKt : StateKt() {
    val stateList: ArrayList<Pair<Drawable, IntArray>> = arrayListOf()
    var item: Pair<Any, IntArray>? = null
        set(value) {
            withNotNull(value) {
                when (first) {
                    is Drawable -> stateList.add(first as Drawable to second)
                    is Int -> {
                        try {
                            when (getResourceType(first as Int)) {
                                "color" -> stateList.add(ColorDrawable(colorRes(first as Int)) to second)
                                "drawable" -> stateList.add(drawableRes(first as Int)!! to second)
                                else -> stateList.add(drawableRes(first as Int)!! to second)
                            }
                        } catch (e: Exception) {
                            stateList.add(ColorDrawable(first as Int) to second)
                        }

                    }
                    else -> {
                        require(false, { "item.first not Drawable or @DrawableRes" })
                    }
                }
            }
        }
}

inline fun selector(b: SelectorKt.() -> Unit): MyStateListDrawable {
    val selectorKt = SelectorKt().apply(b)
    val stateListDrawable = MyStateListDrawable()
    selectorKt.stateList.forEach {
        stateListDrawable.addState(it.second, it.first)
    }
    return stateListDrawable
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

fun LinearLayout.setDividerWidth(width: Int) {
    if (width > 0) {
        showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        dividerDrawable = GradientDrawable().apply {
            val w = (this@setDividerWidth.orientation == LinearLayout.HORIZONTAL)(width)(1)
            val h = (this@setDividerWidth.orientation == LinearLayout.VERTICAL)(width)(1)
            this.setSize(w, h)
        }
    }
}