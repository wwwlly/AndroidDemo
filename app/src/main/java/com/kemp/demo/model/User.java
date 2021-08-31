package com.kemp.demo.model;

import com.kemp.commonlib.piece.PieceName;

/**
 * Created by wangkp on 2018/2/2.
 */

@PieceName(name = "user")
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
