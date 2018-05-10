package com.kemp.demo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kemp.demo.model.Person;
import com.kemp.demo.service.MessengerService;
import com.kemp.demo.service.MyService;
import com.kemp.demo.utils.Constants;

import java.util.List;
import java.util.Random;

/**
 * Created by wangkp on 2018/5/8.
 */

public class IPCDemo extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    private TextView tvContent,tvShowMsg;
    private Button btnAdd,btnSendMsg;

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

    /**
     * 客户端的 Messenger
     */
    Messenger mClientMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            if (msg != null && msg.arg1 == Constants.MSG_ID_SERVER) {
                if (msg.getData() == null) {
                    return;
                }

                String content = (String) msg.getData().get(Constants.MSG_CONTENT);
                Log.d(TAG, "Message from server: " + content);
            }
        }
    });

    //服务端的 Messenger
    private Messenger mServerMessenger;

    private ServiceConnection mMessengerConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            mServerMessenger = new Messenger(service);
            tvShowMsg.setText(tvShowMsg.getText().toString() + " 服务已连接");
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            mServerMessenger = null;
            tvShowMsg.setText(tvShowMsg.getText().toString() + " 服务断开");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipc);

        tvContent = findViewById(R.id.tv_content);
        tvShowMsg = findViewById(R.id.tv_show_msg);
        btnAdd = findViewById(R.id.btn_add);
        btnSendMsg = findViewById(R.id.btn_send);

        btnAdd.setOnClickListener(this);
        btnSendMsg.setOnClickListener(this);

        bindAIDLService();
        bindMessengerService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mAIDLConnection);
        unbindService(mMessengerConnection);
    }

    private void bindAIDLService() {
        Intent intent = new Intent();
//        intent.setComponent(new ComponentName("com.kemp.demo.service", "com.kemp.demo.service.MyService"));
        intent = new Intent(getApplicationContext(), MyService.class);
        bindService(intent, mAIDLConnection, BIND_AUTO_CREATE);
    }

    private void bindMessengerService() {
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mMessengerConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                addPerson();
            default:
                sendMsg();
        }
    }

    private void addPerson() {
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

    private void sendMsg() {
        String msgContent = "这是我发送的消息";
        msgContent = TextUtils.isEmpty(msgContent) ? "默认消息" : msgContent;

        Message message = Message.obtain();
        message.arg1 = Constants.MSG_ID_CLIENT;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MSG_CONTENT, msgContent);
        message.setData(bundle);
        message.replyTo = mClientMessenger;     //指定回信人是客户端定义的

        try {
            mServerMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
