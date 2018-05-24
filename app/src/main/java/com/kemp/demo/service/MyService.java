package com.kemp.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kemp.demo.IMyAidl;
import com.kemp.demo.model.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkp on 2018/5/4.
 */

public class MyService extends Service {

    public static final String TAG = MyService.class.getSimpleName();

    private List<Person> mPersons;

    /**
     * 创建生成的本地 Binder 对象，实现 AIDL 制定的方法
     */
    private IBinder mIBinder = new IMyAidl.Stub() {

        @Override
        public void addPerson(Person person) throws RemoteException {
            Log.d(TAG,"addPerson current thread:" + Thread.currentThread().getName());
            mPersons.add(person);
        }

        @Override
        public List<Person> getPersonList() throws RemoteException {
            Log.d(TAG,"getPersonList current thread:" + Thread.currentThread().getName());
            return mPersons;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind");
        String params = intent.getStringExtra("params");
        Log.d(TAG,"params:" + params);
        mPersons = new ArrayList<>();
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        String params = intent.getStringExtra("params");
        Log.d(TAG,"params:" + params);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }
}
