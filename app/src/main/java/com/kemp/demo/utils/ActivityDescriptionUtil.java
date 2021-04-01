package com.kemp.demo.utils;

import com.kemp.compiler.DescriptionCollector;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class ActivityDescriptionUtil {

    private static ActivityDescriptionUtil instance;

    private ActivityDescriptionUtil(){

    }

    public static ActivityDescriptionUtil getInstance(){
        if (instance == null){
            instance = new ActivityDescriptionUtil();
        }
        return instance;
    }

    private WeakReference<HashMap<String, String>> map;

    public HashMap<String, String> getDescription(){
        if (map == null || map.get() == null){
            HashMap<String, String> m = DescriptionCollector.init();
            map = new WeakReference<>(m);
        }
        return map.get();
    }
}
