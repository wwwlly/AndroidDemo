package com.kemp.demo.utils;

import com.kemp.compiler.DescriptionCollector;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class ActivityDescriptionUtil {

    private static ActivityDescriptionUtil instance;

    private ActivityDescriptionUtil() {

    }

    public static ActivityDescriptionUtil getInstance() {
        if (instance == null) {
            instance = new ActivityDescriptionUtil();
        }
        return instance;
    }

    private WeakReference<HashMap<String, String>> desMap;
    private WeakReference<HashMap<String, String>> labelMap;

    public HashMap<String, String> getDescription() {
        if (desMap == null || desMap.get() == null) {
            HashMap<String, String> m = DescriptionCollector.initDescriptions();
            desMap = new WeakReference<>(m);
        }
        return desMap.get();
    }

    public HashMap<String, String> getLabel() {
        if (labelMap == null || labelMap.get() == null) {
            HashMap<String, String> m = DescriptionCollector.initLabels();
            labelMap = new WeakReference<>(m);
        }
        return labelMap.get();
    }
}
