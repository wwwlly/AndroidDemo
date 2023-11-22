package com.kemp.demo.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.kemp.commonlib.util.SPUtils
import com.kemp.demo.R
import com.kemp.demo.receiver.MyBroadcastReceiver
import com.kemp.demo.utils.Tools
import kotlinx.android.synthetic.main.activity_demo_list.*

class DemoListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_list)

        tv_content.text = SPUtils.getString(MyBroadcastReceiver.KEY_ACTION).let {
            if (it.isEmpty()) "" else it
        }
        tv_content.append("\n")
        tv_content.append("packageName: ${packageName} \n")
        tv_content.append("processName: ${Tools.getProcessName()} \n")

    }

    fun startBootActivity(view: View?) {
        val intent = Intent(this, BootSelfActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun sendBootBroadcast(view: View?) {
//        val intent = Intent(Intent.ACTION_BOOT_COMPLETED)
        val intent = Intent(MyBroadcastReceiver.MY_BROADCAST)
        intent.`package` = packageName
        sendBroadcast(intent)
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}