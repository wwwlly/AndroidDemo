package com.kemp.demo.utils

import android.content.Context
import android.net.Proxy
import android.os.Build
import android.text.TextUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.*

/**
 * 防止apk被抓包
 */
class ProxyTest(val context: Context) {

    companion object {
        const val urlStr = "http://www.baidu.com/"
        const val PROXY_ADDR = "10.168.9.19"
        const val PROXY_PORT = "8888"

        /**
         * 手机是否设置代理
         */
        fun isWifiProxy(context: Context): Boolean {
            val IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
            val proxyAddress: String?
            val proxyPort: Int
            if (IS_ICS_OR_LATER) {
                proxyAddress = System.getProperty("http.proxyHost")
                val portStr = System.getProperty("http.proxyPort")
                proxyPort = (portStr ?: "-1").toInt()
            } else {
                proxyAddress = Proxy.getHost(context)
                proxyPort = Proxy.getPort(context)
            }
            DebugLog.d("Address: $proxyAddress, Port: $proxyPort")
            return !TextUtils.isEmpty(proxyAddress) && proxyPort != -1
        }

    }

    fun testIsWifiProxy() {
        DebugLog.d(if (isWifiProxy(context)) "开启代理" else "未开启代理")
    }

    private fun setDefaultProxy() {
        ProxySelector.setDefault(object : ProxySelector() {
            override fun select(uri: URI?): MutableList<java.net.Proxy> {
                DebugLog.d("ProxySelector select")
                val list = mutableListOf<java.net.Proxy>()
                list.add(Proxy(java.net.Proxy.Type.HTTP, InetSocketAddress(PROXY_ADDR, PROXY_PORT.toInt())))
                return list
            }

            override fun connectFailed(uri: URI?, sa: SocketAddress?, ioe: IOException?) {
                DebugLog.d("无法连接到服务器")
            }

        })
    }

    private fun requestHttpURLConnection(defaultProxy: Boolean = false) {
        if (defaultProxy) setDefaultProxy()

        try {
            val url = URL(urlStr)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val result = is2String(connection.inputStream)
                DebugLog.d("result: $result")
            } else {
                DebugLog.d("请求失败")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun is2String(inputStream: InputStream): String {

        val bufferedReader = BufferedReader(InputStreamReader(inputStream, "utf-8"))
        val stringBuilder = StringBuilder()
        do {
            val line = bufferedReader.readLine()
            if (line != null) stringBuilder.append(line)
        } while (line != null)
        return stringBuilder.toString().trim()
    }

    private fun requestOkHttp() {
//        val okHttpClient = OkHttpClient()
        val okHttpClient = OkHttpClient().newBuilder().proxy(java.net.Proxy.NO_PROXY).build()
        val request = Request.Builder().url(urlStr).get().build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                DebugLog.d("result: ${response.body?.string()}")
            }

            override fun onFailure(call: Call, e: IOException) {
                DebugLog.d("请求失败")
            }
        })
    }

    fun testRequestHttpURLConnection() {
        GlobalScope.async {
            requestHttpURLConnection()
        }
        DebugLog.d("开始请求")
    }

    fun testRequestOkHttp() {
        requestOkHttp()
        DebugLog.d("开始请求")
    }
}