package com.kemp.demo.extension;

import com.kemp.annotations.ActionFilter;
import com.kemp.demo.utils.DebugLog;

public class TestExtension {

    @ActionFilter
    public String testExtension(String id, String params) {
        DebugLog.d("id : " + id + " params: " + params);
        return "";
    }
}
