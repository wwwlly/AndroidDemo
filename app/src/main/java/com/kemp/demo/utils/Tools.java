package com.kemp.demo.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;

public class Tools {

    public static final String TAG = "Tools";

    public static void printSpecMode(String pre, int spec) {
        if (TextUtils.isEmpty(pre)) {
            pre = "";
        }
        int specMode = View.MeasureSpec.getMode(spec);
        switch (specMode) {
            case View.MeasureSpec.EXACTLY:
                Log.d(TAG, pre + "specMode is EXACTLY");
                break;
            case View.MeasureSpec.AT_MOST:
                Log.d(TAG, pre + "specMode is AT_MOST");
                break;
            case View.MeasureSpec.UNSPECIFIED:
                Log.d(TAG, pre + "specMode is UNSPECIFIED");
                break;
            default:
                Log.d(TAG, pre + "specMode is unknown");
                break;
        }
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static DisplayMetrics getDisplayRealMetrics(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        return dm;
    }

}
