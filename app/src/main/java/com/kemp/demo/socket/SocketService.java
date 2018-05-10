package com.kemp.demo.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by wangkp on 2018/5/10.
 */

public class SocketService extends Service {
    private final String TAG = this.getClass().getSimpleName();

    private boolean mIsServiceDisconnected;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "服务已 create");
        new Thread(new TCPServer()).start();
    }


    private class TCPServer implements Runnable {
        @Override
        public void run() {
            ServerSocket serverSocket;
            try {
                serverSocket = new ServerSocket(SocketConfig.TEST_SOCKET_PORT);
                Log.d(TAG, "TCP 服务已创建");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("TCP 服务端创建失败");
                return;
            }

            while (!mIsServiceDisconnected) {
                try {
                    Socket client = serverSocket.accept();  //接受客户端消息，阻塞直到收到消息

//                    new Thread(responseClient(client)).start();
                    ThreadPoolManager.getInstance()
                            .addTask(responseClient(client));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Runnable responseClient(final Socket client) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    //接受消息
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    //回复消息
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
                    out.println("服务端已连接 *****");

                    while (!mIsServiceDisconnected) {
                        String inputStr = in.readLine();
                        Log.i(TAG, "收到客户端的消息：" + inputStr);
                        if (TextUtils.isEmpty(inputStr)) {
                            Log.i(TAG, "收到消息为空，客户端断开连接 ***");
                            break;
                        }
                        out.println("你这句【" + inputStr + "】非常有道理啊！");
                    }
                    out.close();
                    in.close();
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        mIsServiceDisconnected = true;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
