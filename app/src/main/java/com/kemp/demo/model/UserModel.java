package com.kemp.demo.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * Created by wangkp on 2018/2/5.
 */

public class UserModel extends AndroidViewModel {

    public UserModel(@NonNull Application application) {
        super(application);
    }

    public void click(){
        Toast.makeText(this.getApplication(), "click button", Toast.LENGTH_SHORT).show();
    }

    public String getButtonText(){
        return "click me";
    }
}
