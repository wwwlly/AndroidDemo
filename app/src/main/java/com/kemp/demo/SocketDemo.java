package com.kemp.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kemp.demo.utils.Constants;
import com.kemp.demo.socket.SocketService;
import com.kemp.demo.socket.ThreadPoolManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wangkp on 2018/5/10.
 */

public class SocketDemo extends AppCompatActivity {

    private EditText etInput;
    private TextView tvShow;
    private Button btnSend;

    private Socket mClientSocket;
    private PrintWriter mPrintWriter;
    private SocketHandler mSocketHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        etInput = findViewById(R.id.et_input);
        tvShow = findViewById(R.id.tv_show);
        btnSend = findViewById(R.id.btn_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsgToSocketServer();
            }
        });

        startSocketService();
    }

    private void startSocketService() {
        Intent intent = new Intent(this, SocketService.class);
        startService(intent);

        mSocketHandler = new SocketHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connectSocketServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 处理 Socket 线程切换
     */
    @SuppressWarnings("HandlerLeak")
    public class SocketHandler extends Handler {
        public static final int CODE_SOCKET_CONNECT = 1;
        public static final int CODE_SOCKET_MSG = 2;

        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case CODE_SOCKET_CONNECT:
                    btnSend.setEnabled(true);
                    break;
                case CODE_SOCKET_MSG:
                    tvShow.append((String) msg.obj);
                    break;
            }
        }
    }

    /**
     * 通过 Socket 连接服务端
     */
    private void connectSocketServer() throws IOException {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("localhost", Constants.TEST_SOCKET_PORT);
                mClientSocket = socket;
                mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            } catch (IOException e) {
                SystemClock.sleep(1_000);
            }
        }

        mSocketHandler.sendEmptyMessage(SocketHandler.CODE_SOCKET_CONNECT);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (!isFinishing()) {
            final String msg = in.readLine();
            if (!TextUtils.isEmpty(msg)) {
                mSocketHandler.obtainMessage(SocketHandler.CODE_SOCKET_MSG,
                        "\n" + getCurrentTime() + "\nserver : " + msg)
                        .sendToTarget();
            }
            SystemClock.sleep(1_000);
        }

        System.out.println("Client quit....");
        mPrintWriter.close();
        in.close();
        socket.close();
    }

    public void sendMsgToSocketServer() {
        final String msg = etInput.getText().toString();
        if (!TextUtils.isEmpty(msg) && mPrintWriter != null) {
            ThreadPoolManager.getInstance().addTask(new Runnable() {
                @Override
                public void run() {
                    mPrintWriter.println(msg);
                }
            });
            etInput.setText("");
            tvShow.append("\n" + getCurrentTime() + "\nclient : " + msg);
        }
    }

    public static String getCurrentTime() {
        return getDateString(System.currentTimeMillis());
    }

    public static String getDateString(long timeMillis) {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())).format(new Date(timeMillis));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mClientSocket != null) {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
