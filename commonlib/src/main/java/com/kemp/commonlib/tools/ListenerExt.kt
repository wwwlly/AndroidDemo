package com.kemp.commonlib.tools

import android.animation.Animator
import android.animation.ValueAnimator
import android.support.v4.view.ViewPager
import android.text.Editable
import android.text.TextWatcher
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by yinzj on 2018/10/19.
 */
class FloatAnimator : ValueAnimator()

fun floatAnimator(duration: Long = 300, onUpdate: FloatAnimator.(Float) -> Unit) = FloatAnimator().apply {
    setFloatValues(0f, 1f)
    this.duration = duration
    addUpdateListener {
        onUpdate(this, it.animatedValue as Float)
    }
}

open class AnimListener : Animator.AnimatorListener, Animation.AnimationListener {

    private var onAnimatorRepeat: ((Animator?) -> Unit)? = null
    private var onAnimatorEnd: ((Animator?) -> Unit)? = null
    private var onAnimatorCancel: ((Animator?) -> Unit)? = null
    private var onAnimatorStart: ((Animator?) -> Unit)? = null

    private var onAnimationStart: ((Animation?) -> Unit)? = null
    private var onAnimationEnd: ((Animation?) -> Unit)? = null
    private var onAnimationRepeat: ((Animation?) -> Unit)? = null

    override fun onAnimationRepeat(animation: Animator?) {
        onAnimatorRepeat?.invoke(animation)
    }

    override fun onAnimationEnd(animation: Animator?) {
        onAnimatorEnd?.invoke(animation)
    }

    override fun onAnimationCancel(animation: Animator?) {
        onAnimatorCancel?.invoke(animation)
    }


    override fun onAnimationStart(animation: Animator?) {
        onAnimatorStart?.invoke(animation)
    }


    fun onAnimatorRepeat(b: (Animator?) -> Unit) {
        onAnimatorRepeat = b
    }

    fun onAnimatorEnd(b: (Animator?) -> Unit) {
        onAnimatorEnd = b
    }

    fun onAnimatornCancel(b: (Animator?) -> Unit) {
        onAnimatorCancel = b
    }


    fun onAnimatorStart(b: (Animator?) -> Unit) {
        onAnimatorStart = b
    }


    fun onAnimationRepeat(b: (Animation?) -> Unit) {
        onAnimationRepeat = b
    }

    fun onAnimationEnd(b: (Animation?) -> Unit) {
        onAnimationEnd = b
    }

    fun onAnimationStart(b: (Animation?) -> Unit) {
        onAnimationStart = b
    }

    override fun onAnimationRepeat(animation: Animation?) {
        onAnimationRepeat?.invoke(animation)
    }

    override fun onAnimationEnd(animation: Animation?) {
        onAnimationEnd?.invoke(animation)
    }

    override fun onAnimationStart(animation: Animation?) {
        onAnimationStart?.invoke(animation)
    }
}

inline fun Animator.onListener(l: AnimListener.() -> Unit) = addListener(AnimListener().apply(l))
inline val Animator.listener: AnimListener
    get() {
        val listener = AnimListener()
        addListener(listener)
        return listener
    }

inline fun Animation.onListener(l: AnimListener.() -> Unit) = setAnimationListener(AnimListener().apply(l))
inline val Animation.listener: AnimListener
    get() {
        val listener = AnimListener()
        setAnimationListener(listener)
        return listener
    }

class GestureListener : GestureDetector.SimpleOnGestureListener() {

    private var onSingleTapUp: ((MotionEvent?) -> Boolean)? = null
    private var onDown: ((MotionEvent?) -> Boolean)? = null
    private var onFling: ((e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float) -> Boolean)? = null
    private var onDoubleTap: ((MotionEvent?) -> Boolean)? = null
    private var onScroll: ((e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float) -> Boolean)? = null
    private var onContextClick: ((MotionEvent?) -> Boolean)? = null
    private var onSingleTapConfirmed: ((MotionEvent?) -> Boolean)? = null
    private var onShowPress: ((MotionEvent?) -> Unit)? = null
    private var onDoubleTapEvent: ((MotionEvent?) -> Boolean)? = null
    private var onLongPress: ((MotionEvent?) -> Unit)? = null

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return onSingleTapUp?.invoke(e) ?: super.onSingleTapUp(e)
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return onDown?.invoke(e) ?: super.onDown(e)
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        return onFling?.invoke(e1, e2, velocityX, velocityY)
                ?: super.onFling(e1, e2, velocityX, velocityY)
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        return onDoubleTap?.invoke(e) ?: super.onDoubleTap(e)
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return onScroll?.invoke(e1, e2, distanceX, distanceY)
                ?: super.onScroll(e1, e2, distanceX, distanceY)
    }

    override fun onContextClick(e: MotionEvent?): Boolean {
        return onContextClick?.invoke(e) ?: super.onContextClick(e)
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        return onSingleTapConfirmed?.invoke(e) ?: super.onSingleTapConfirmed(e)
    }

    override fun onShowPress(e: MotionEvent?) {
        onShowPress?.invoke(e)
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return onDoubleTapEvent?.invoke(e) ?: super.onDoubleTapEvent(e)
    }

    override fun onLongPress(e: MotionEvent?) {
        onLongPress?.invoke(e)
    }

    fun onSingleTapUp(l: (e: MotionEvent?) -> Boolean) {
        onSingleTapUp = l
    }

    fun onDown(l: (e: MotionEvent?) -> Boolean) {
        onDown = l
    }

    fun onFling(l: (e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float) -> Boolean) {
        onFling = l
    }

    fun onDoubleTap(l: (e: MotionEvent?) -> Boolean) {
        onDoubleTap = l
    }

    fun onScroll(l: (e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float) -> Boolean) {
        onScroll = l
    }

    fun onContextClick(l: (e: MotionEvent?) -> Boolean) {
        onContextClick = l
    }

    fun onSingleTapConfirmed(l: (e: MotionEvent?) -> Boolean) {
        onSingleTapConfirmed = l
    }

    fun onShowPress(l: (e: MotionEvent?) -> Unit) {
        onShowPress = l
    }

    fun onDoubleTapEvent(l: (e: MotionEvent?) -> Boolean) {
        onDoubleTapEvent = l
    }

    fun onLongPress(l: (e: MotionEvent?) -> Unit) {
        onLongPress = l
    }
}

fun View.onGestureListener(l: GestureListener.() -> Boolean) {
    setOnTouchListener { _, event ->
        val gestureDetectorListener = GestureListener()
        GestureDetector(context, gestureDetectorListener).apply { setOnDoubleTapListener(gestureDetectorListener) }.onTouchEvent(event)
        l(gestureDetectorListener)
    }
}

inline val View.gestureListener: GestureListener
    get() {
        val listener = GestureListener()
        setOnTouchListener { v, event ->
            GestureDetector(context, listener).apply { setOnDoubleTapListener(listener) }.onTouchEvent(event)
        }
        return listener
    }

fun View.click(l: (View) -> Unit) = setOnClickListener(l)
fun View.longClick(l: (View) -> Boolean) = setOnLongClickListener(l)

class PageChangeListener : ViewPager.OnPageChangeListener {

    private var onPageScrollStateChanged: ((state: Int) -> Unit)? = null
    private var onPageScrolled: ((position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit)? = null
    private var onPageSelected: ((position: Int) -> Unit)? = null

    override fun onPageScrollStateChanged(state: Int) {
        onPageScrollStateChanged?.invoke(state)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        onPageScrolled?.invoke(position, positionOffset, positionOffsetPixels)
    }

    override fun onPageSelected(position: Int) {
        onPageSelected?.invoke(position)
    }

    fun onPageScrollStateChanged(l: ((state: Int) -> Unit)) {
        onPageScrollStateChanged = l
    }

    fun onPageScrolled(l: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit) {
        onPageScrolled = l
    }

    fun onPageSelected(l: ((position: Int) -> Unit)) {
        onPageSelected = l
    }
}

fun ViewPager.OnPageChange(l: PageChangeListener.() -> Unit) = addOnPageChangeListener(PageChangeListener().apply(l))

open class MyObserver<T> : Observer<T> {

    var onNext: ((T) -> Unit)? = null
    var onComplete: (() -> Unit)? = null
    var onError: ((Throwable) -> Unit)? = null
    var onSubscribe: ((Disposable) -> Unit)? = null

    fun onNext(l: (T) -> Unit) {
        onNext = l
    }

    fun onComplete(l: () -> Unit) {
        onComplete = l
    }

    fun onError(l: (Throwable) -> Unit) {
        onError = l

    }

    fun onSubscribe(l: (Disposable) -> Unit) {
        onSubscribe = l
    }

    override fun onError(e: Throwable) {
        onError?.invoke(e)
        onComplete()
    }

    override fun onNext(t: T) {
        onNext?.invoke(t)
    }

    override fun onComplete() {
        onComplete?.invoke()
    }

    override fun onSubscribe(d: Disposable) {
        onSubscribe?.invoke(d)
    }

}

fun <T> Observable<T>.observer(observer: MyObserver<T>.() -> Unit) {
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(MyObserver<T>().apply(observer))
}



open class TextChangedListener : TextWatcher {

    private var afterTextChanged: ((s: Editable?) -> Unit)? = null
    private var beforeTextChanged: ((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null
    private var onTextChanged: ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null

    override fun afterTextChanged(s: Editable?) {
        afterTextChanged?.invoke(s)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        beforeTextChanged?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChanged?.invoke(s, start, before, count)
    }

    fun afterTextChanged(l:((s: Editable?) -> Unit)) {
        afterTextChanged = l
    }

    fun beforeTextChanged(l:((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)) {
        beforeTextChanged = l
    }

    fun onTextChanged(l:((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)) {
        onTextChanged = l
    }
}

fun TextView.addTextChanged(l:TextChangedListener.()->Unit) = addTextChangedListener(TextChangedListener().apply(l))