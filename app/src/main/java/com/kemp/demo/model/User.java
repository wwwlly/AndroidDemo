package com.kemp.demo.model;

/**
 * Created by wangkp on 2018/2/2.
 */

public class User {
    public final String firstName;
    public final String lastName;
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void onRefresh() {
    }
}
