package com.kemp.demo.utils;

import android.text.TextUtils;
import android.util.Log;
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
}
