package com.kemp.demo.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class MarketLayoutManager extends RecyclerView.LayoutManager {

    private static final String TAG = "MarketLayoutManager";
    private int columns = 2;

    private int[] horiIndexes;

    private int offsetVertical = 0;
    private int totalHeight = 0;

    public MarketLayoutManager(int[] horiIndexes) {
        this.horiIndexes = horiIndexes;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new GridLayoutManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        Log.d(TAG, "onMeasure totalHeight " + totalHeight);
        int width = View.MeasureSpec.getSize(widthSpec);
        int modeW = View.MeasureSpec.getMode(widthSpec);
        int height = View.MeasureSpec.getSize(heightSpec);
        int modeH = View.MeasureSpec.getMode(heightSpec);
        if (modeH == View.MeasureSpec.EXACTLY && height > 0) {
            super.onMeasure(recycler, state, widthSpec, View.MeasureSpec.makeMeasureSpec(totalHeight, View.MeasureSpec.EXACTLY));
        } else {
            setMeasuredDimension(width, totalHeight);
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        Log.d(TAG, "======" + getWidth() + "," + getHeight());
        if (getItemCount() <= 0) {
            return;
        }
        detachAndScrapAttachedViews(recycler);

        fillChildren(recycler, state);
    }

    private void fillChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int l = 0, t = 0, r = 0, b = 0;
        int horiLayoutIndex = 0;
        for (int i = 0; i < getItemCount(); i++) {
            View child = recycler.getViewForPosition(i);

            addView(child);
            measureChildWithMargins(child, 0, 0);

            int width = getDecoratedMeasuredWidth(child);
            int height = getDecoratedMeasuredHeight(child);

            if (isLayoutFillParent(i)) {//横向占满全屏
                Log.d(TAG, "layout fill");
                horiLayoutIndex = 0;
                l = 0;
                r = getWidth();
                t = b;
                b = t + height;
            } else {//横向平分
                Log.d(TAG, "layout half");
                int horiW = getWidth() / columns;
                if (horiLayoutIndex < columns) {
                    l = horiLayoutIndex * horiW;
                    r = l + horiW;
                    if (horiLayoutIndex == 0) {
                        t = b;
                        b = t + height;
                    }
                }
                horiLayoutIndex++;
                if (horiLayoutIndex >= columns) {
                    horiLayoutIndex = 0;
                }
            }
            layoutDecoratedWithMargins(child, l, t - offsetVertical, r, b - offsetVertical);
            Log.d(TAG, "layout: " + l + ", " + t + ", " + r + ", " + b);
        }
        totalHeight = b;
    }

    private boolean isLayoutFillParent(int index) {
        if (horiIndexes == null) {
            return false;
        }
        for (int i : horiIndexes) {
            if (index == i) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() == 0) {
            return 0;
        }

        Log.d(TAG, "dy: " + dy);
        if (dy < 0 && offsetVertical <= 0) {
            return 0;
        }
        Log.d(TAG, "offsetVertical " + offsetVertical + ",totalHeight " + totalHeight + ",height " + getHeight());
        if (dy > 0 && (totalHeight < getHeight() || offsetVertical > totalHeight - getHeight())) {
            return 0;
        }
        detachAndScrapAttachedViews(recycler);

        offsetVertical += dy;
        fillChildren(recycler, state);
        return dy;
    }

    static class LayoutState {

        private int offsetVertical = 0;
        private int totalHeight = 0;


    }
}
