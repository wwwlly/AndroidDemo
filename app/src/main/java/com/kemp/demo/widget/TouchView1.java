package com.kemp.demo.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.kemp.demo.utils.Tools;

public class TouchView1 extends ViewGroup {

    private static final String TAG = "TouchDemo";

//    HorizontalScrollView
//    ScrollView
//    OnTouchListener
//    Scroller
//    NestedScrollingParent
//    ConstraintLayout
//    RecyclerView
//    NestedScrollView

    private int mLastInterX, mLastInterY;

    private Scroller mScroller;

    private int mOverscrollDistance;

    public TouchView1(Context context, AttributeSet attrs) {
        super(context, attrs);

//        Object sRecycleLock = new Object[0];
//        Object sRecycleLock1 = new Object();
        mScroller = new Scroller(getContext());

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mOverscrollDistance = configuration.getScaledOverscrollDistance();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Tools.printSpecMode("width", widthMeasureSpec);
        Tools.printSpecMode("height", heightMeasureSpec);
        measureHorizontal(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutHorizontal(l, t, r, b);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

//        boolean intercept = false;
//        int x = (int) ev.getX();
//        int y = (int) ev.getY();
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                intercept = false;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int dx = mLastInterX - x;
//                int dy = mLastInterY - y;
//                if (Math.abs(dx) > Math.abs(dy)) {
//                    intercept = true;
//                } else {
//                    intercept = false;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                intercept = false;
//                break;
//        }
//        mLastInterX = x;
//        mLastInterY = y;
//        return intercept;
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                mLastInterX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                x = (int) event.getX();
                int deltaX = mLastInterX - x;
                overScrollBy(deltaX, 0, getScrollX(), 0, getScrollRange(), 0, mOverscrollDistance, 0, true);
                mLastInterX = x;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        int total = 0;
        int maxHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            measureChildWithMargins(child, widthMeasureSpec, total, heightMeasureSpec, 0);
            int childWidth = child.getMeasuredWidth();
            Log.d(TAG, "child index:" + i + ",childWidth:" + childWidth);
            total += childWidth;
            int clientHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            maxHeight = Math.max(maxHeight, clientHeight);
        }
        setMeasuredDimension(resolveSize(total, widthMeasureSpec), resolveSize(maxHeight, heightMeasureSpec));
    }

    private void layoutHorizontal(int left, int top, int right, int bottom) {
        int childTop = 0;
        int childLeft = getPaddingLeft();

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            childLeft += lp.leftMargin;
            childTop = lp.topMargin;
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            setChildFrame(child, childLeft, childTop, childWidth, childHeight);
            childLeft += childWidth + lp.rightMargin;
        }
    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return super.generateLayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    public void computeScroll() {
//        overScrollBy(x - oldX, y - oldY, oldX, oldY, range, 0,
//                mOverflingDistance, 0, false);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        scrollTo(scrollX, scrollY);
    }

    protected void measureChildWithMargins(View child,
                                           int parentWidthMeasureSpec, int widthUsed,
                                           int parentHeightMeasureSpec, int heightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin
                        + widthUsed, lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin
                        + heightUsed, lp.height);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
        int specMode = MeasureSpec.getMode(spec);
        int specSize = MeasureSpec.getSize(spec);

        int size = Math.max(0, specSize - padding);

        int resultSize = specSize;
        int resultMode = MeasureSpec.UNSPECIFIED;

        switch (specMode) {
            // Parent has imposed an exact size on us
            case MeasureSpec.EXACTLY:
                Log.d(TAG, "specMode is EXACTLY");
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == ViewGroup.LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size. So be it.
                    resultSize = size;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size. It can't be
                    // bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;

            // Parent has imposed a maximum size on us
            case MeasureSpec.AT_MOST:
                Log.d(TAG, "specMode is AT_MOST");
//                if (childDimension >= 0) {
//                    // Child wants a specific size... so be it
//                    resultSize = childDimension;
//                    resultMode = MeasureSpec.EXACTLY;
//                } else if (childDimension == ViewGroup.LayoutParams.MATCH_PARENT) {
//                    // Child wants to be our size, but our size is not fixed.
//                    // Constrain child to not be bigger than us.
//                    resultSize = size;
//                    resultMode = MeasureSpec.AT_MOST;
//                } else if (childDimension == ViewGroup.LayoutParams.WRAP_CONTENT) {
//                    // Child wants to determine its own size. It can't be
//                    // bigger than us.
//                    resultSize = size;
//                    resultMode = MeasureSpec.AT_MOST;
//                }
                break;

            // Parent asked to see how big we want to be
            case MeasureSpec.UNSPECIFIED:
                Log.d(TAG, "specMode is UNSPECIFIED");
//                if (childDimension >= 0) {
//                    // Child wants a specific size... let him have it
//                    resultSize = childDimension;
//                    resultMode = MeasureSpec.EXACTLY;
//                } else if (childDimension == ViewGroup.LayoutParams.MATCH_PARENT) {
//                    // Child wants to be our size... find out how big it should
//                    // be
//                    resultSize = size;
//                    resultMode = MeasureSpec.UNSPECIFIED;
//                } else if (childDimension == ViewGroup.LayoutParams.WRAP_CONTENT) {
//                    // Child wants to determine its own size.... find out how
//                    // big it should be
//                    resultSize = size;
//                    resultMode = MeasureSpec.UNSPECIFIED;
//                }
                break;
        }
        //noinspection ResourceType
        return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }

    private int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0,
                    child.getWidth() - (getWidth() - getPaddingLeft() - getPaddingRight()));
        }
        return scrollRange;
    }
}
