package com.kemp.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kemp.commonlib.piece.PieceName;

/**
 * Created by wangkp on 2018/5/8.
 */

@PieceName(name = "person_list")
public class Person implements Parcelable {
    private String mName;

    public Person(String name) {
        mName = name;
    }

    protected Person(Parcel in) {
        mName = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
    }

//    @Override
//    public String toString() {
//        return "Person{" +
//                "mName='" + mName + '\'' +
//                '}';
//    }


    @Override
    public String toString() {
        return "Person{" +
                "mName='" + mName + '\'' +
                '}';
    }
}
