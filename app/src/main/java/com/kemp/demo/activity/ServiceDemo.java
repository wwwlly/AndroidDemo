package com.kemp.demo.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.kemp.demo.R;
import com.kemp.demo.service.MyService;

/**
 * Created by wangkp on 2018/5/4.
 */

public class ServiceDemo extends AppCompatActivity {

    private boolean bind = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

    }

    public void onClick(View view) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("params","params");
        switch (view.getId()) {
            case R.id.btn_start:
                startService(intent);
                break;
            case R.id.btn_stop:
                stopService(intent);
                break;
            case R.id.btn_bind:
                bind = bindService(intent,connection,BIND_AUTO_CREATE);
                break;
            case R.id.btn_unbind:
                if(bind){
                    unbindService(connection);
                    bind = false;
                }
                break;
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(MyService.TAG,"onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(MyService.TAG,"onServiceDisconnected");
        }
    };
}
