package com.kemp.demo.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Path
import android.graphics.PathMeasure
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.kemp.commonlib.tools.colorInt
import com.kemp.commonlib.tools.dp
import com.kemp.demo.R
import com.noober.background.drawable.DrawableCreator

object AnimatorUtil {

    fun startAddAnimator(activity: Activity?, contentView: ViewGroup?, startView: View?, endView: View?) {
        if (activity == null || activity.isFinishing || contentView == null || startView == null || endView == null) return

        val targetView = createPointView(activity)
        val layoutParams = ViewGroup.LayoutParams(5.dp, 5.dp)
        contentView.addView(targetView, layoutParams)

        val contentLoc = IntArray(2)
        contentView.getLocationInWindow(contentLoc)
        val startLoc = IntArray(2)
        startView.getLocationInWindow(startLoc)
        val endLoc = IntArray(2)
        endView.getLocationInWindow(endLoc)

        val startX = (startLoc[0] - contentLoc[0] + targetView.width / 2).toFloat()
        val startY = (startLoc[1] - contentLoc[1] + targetView.height / 2).toFloat()
        val endX = (endLoc[0] - contentLoc[0] + endView.width / 2).toFloat()
        val endY = (endLoc[1] - contentLoc[1]).toFloat()

        val path = Path().apply {
            moveTo(startX, startY)
            quadTo((startX + endX) / 2, startY, endX, endY)
        }
        val pathMeasure = PathMeasure(path, false)

        val currentLoc = FloatArray(2)
        val valueAnimator = ValueAnimator.ofFloat(0f, pathMeasure.length).apply {
            duration = 300
            interpolator = LinearInterpolator()
            addUpdateListener {
                pathMeasure.getPosTan(it?.animatedValue as Float, currentLoc, null)
                targetView.translationX = currentLoc[0]
                targetView.translationY = currentLoc[1]
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    contentView.removeView(targetView)
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationRepeat(animation: Animator?) {}

            })
        }
        valueAnimator.start()
    }

    private fun createPointView(activity: Activity?): View {
        val drawable = DrawableCreator.Builder().setShape(DrawableCreator.Shape.Oval).setSolidColor(R.color.red.colorInt).build()
        return ImageView(activity).apply { setImageDrawable(drawable) }
    }
}