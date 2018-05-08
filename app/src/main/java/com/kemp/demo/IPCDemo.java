package com.kemp.demo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kemp.demo.model.Person;
import com.kemp.demo.service.MyService;

import java.util.List;
import java.util.Random;

/**
 * Created by wangkp on 2018/5/8.
 */

public class IPCDemo extends AppCompatActivity implements View.OnClickListener {

    private TextView tvContent;
    private Button btnAdd;

    private IMyAidl mAidl;

    private ServiceConnection mAIDLConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接后拿到 Binder，转换成 AIDL，在不同进程会返回个代理
            mAidl = IMyAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAidl = null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipc);

        tvContent = findViewById(R.id.tv_content);
        btnAdd = findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(this);

        bindAIDLService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mAIDLConnection);
    }

    private void bindAIDLService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.kemp.demo.service", "com.kemp.demo.service.MyService"));
        bindService(intent, mAIDLConnection, BIND_AUTO_CREATE);

        Intent intent1 = new Intent(getApplicationContext(), MyService.class);
        bindService(intent1, mAIDLConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        Random random = new Random();
        Person person = new Person("www" + random.nextInt(10));

        try {
            mAidl.addPerson(person);
            List<Person> personList = mAidl.getPersonList();
            tvContent.setText(personList.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
