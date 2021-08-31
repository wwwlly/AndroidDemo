package com.kemp.commonlib.util;

import android.text.TextUtils;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonUtils {
    private static Gson sGson;

    static {
        sGson = new Gson();
    }

    public static <T> T parse(String strDataJson, Class<T> classOfT) {
        T data = null;
        if (!TextUtils.isEmpty(strDataJson)) {
            try {
                data = sGson.fromJson(strDataJson, classOfT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static <T> T parse(byte[] dataJson, Class<T> classOfT) {
        T data = null;
        String strDataJson = null;
        try {
            strDataJson = new String(dataJson, "utf-8");
            data = parse(strDataJson, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static <T> ArrayList<T> parseList(String strDataJson, Type type) {
        ArrayList<T> list = null;
        if (null != strDataJson) {
            try {
                list = sGson.fromJson(strDataJson, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public static String toGson(Object obj) {
        if (obj != null) {
            return sGson.toJson(obj);
        }
        return "";
    }

    public static Gson getGson() {
        return sGson;
    }
}
