package com.kemp.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kemp.demo.utils.Constants;

/**
 * Created by wangkp on 2018/5/10.
 */

public class MessengerService extends Service {
    private final String TAG = this.getClass().getSimpleName();

    Messenger mMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            if (msg != null && msg.arg1 == Constants.MSG_ID_CLIENT) {
                if (msg.getData() == null) {
                    return;
                }
                String content = (String) msg.getData().get(Constants.MSG_CONTENT);  //接收客户端的消息
                Log.d(TAG, "Message from client: " + content);

                //回复消息给客户端
                Message replyMsg = Message.obtain();
                replyMsg.arg1 = Constants.MSG_ID_SERVER;
                Bundle bundle = new Bundle();
                bundle.putString(Constants.MSG_CONTENT, "听到你的消息了，请说点正经的");
                replyMsg.setData(bundle);

                try {
                    msg.replyTo.send(replyMsg);     //回信
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return mMessenger.getBinder();
    }
}
