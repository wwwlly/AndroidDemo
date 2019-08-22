package com.kemp.demo.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.kemp.demo.R;

public class WebViewDemo extends AppCompatActivity {

    private static final String TAG = "WebViewDemo";

    private long startTime = 0L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        startTime = System.currentTimeMillis();
        WebView webView = findViewById(R.id.web_view);
        setWebSettings(webView);
//        webView.loadUrl("http://zhinengop.yichehuoban.cn/chexian/login");
//        webView.loadUrl("http://192.168.15.16:9001/h5/");
//        webView.loadUrl("http://uniapp.dcloud.io/h5/");
        webView.loadUrl("https://mobile.ant.design/kitchen-sink/");
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
    }

    private void setWebSettings(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setSupportZoom(false);
        webSettings.setTextZoom(100);//添加了之后手机改变字体大小不会影响H5显示
        webSettings.setJavaScriptEnabled(true);// 启用javascript
        webSettings.setAllowFileAccess(false);// 需要使用 file 协议
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(false);
            webSettings.setAllowUniversalAccessFromFileURLs(false);
        }
        //webview在安卓5.0之前默认允许其加载混合网络协议内容,在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings.setMediaPlaybackRequiresUserGesture(false);//设置允许自动播放音视频
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
//            loadFinish();
        }
    }

    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.d(TAG, "newProgress: " + newProgress);
            if (newProgress == 100) {
                loadFinish();
            }
        }
    }

    private void loadFinish(){
        long endTime = System.currentTimeMillis();
        double p = (endTime - startTime) / 1000d;
        String str = String.format("启动时间：%ss", p);
        Log.d(TAG, str);
        Toast.makeText(WebViewDemo.this, str, Toast.LENGTH_LONG).show();
    }
}
