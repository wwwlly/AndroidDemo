package com.kemp.demo.dragger2;

import android.util.Log;

import com.kemp.demo.Dagger2Demo;

import javax.inject.Inject;

/**
 * Created by wangkp on 2018/2/24.
 */

public class DaggerA {

    @Inject
    DaggerB daggerB;

    DaggerA(){

    }

    @Inject
    DaggerA(DaggerB daggerB){
        this.daggerB = daggerB;
        Log.d(Dagger2Demo.TAG,daggerB == null ? "daggerB is null": "daggerB is not null");
    }

    public String testDaggerA(){
        return "test dagger a";
    }
}
