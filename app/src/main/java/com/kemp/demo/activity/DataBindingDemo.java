package com.kemp.demo.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.kemp.demo.R;
import com.kemp.demo.databinding.ActivityDataBindingBinding;
import com.kemp.demo.model.UserModel;

/**
 * Created by wangkp on 2018/2/2.
 */

public class DataBindingDemo extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDataBindingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding);
//        ActivityDataBindingBinding.inflate

        UserModel userModel = ViewModelProviders.of(this).get(UserModel.class);
        binding.setUserModel(userModel);
    }
}
