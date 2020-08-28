package com.kemp.demo.dragger2;

import android.util.Log;

import javax.inject.Inject;

public class Hero {

    private static final String TAG = "Hero";
    private Clothes clothes;
    private Pants pants;

    @Inject
    public Hero(Clothes clothes, Pants pants){
        this.clothes = clothes;
        this.pants = pants;
    }

    public void printDefense() {
        Log.e(TAG, "您的角色拥有防御值: " + (clothes.Defense + pants.Defense));
    }
}
