package com.kemp.demo.extension;

import com.kemp.annotations.AutoExtension;

import org.json.JSONObject;

@AutoExtension
public interface ReportTinyDataPoint extends Extension {

    void reportTinyData(JSONObject params, Object page);
}
