package com.kemp.demo.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.kemp.commonlib.util.SPUtils
import com.kemp.demo.activity.BootSelfActivity
import com.kemp.demo.utils.DebugLog


class MyBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val TAG = "BROADCAST"
        const val KEY_ACTION = "myBroadcastAction"
        const val MY_BROADCAST = "com.kemp.demo.MY_BROADCAST"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return

        DebugLog.d(TAG, "MyBroadcastReceiver onReceive action: ${intent.action}")
        SPUtils.putString(KEY_ACTION, intent.action)

        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                Toast.makeText(context, "MyBroadcastReceiver 开机", Toast.LENGTH_LONG).show()

                startBootActivity(context)
            }

            MY_BROADCAST -> {
                startBootActivity(context)
            }
        }
    }

    private fun startBootActivity(context: Context?) {
        val intent = Intent(context, BootSelfActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(intent)
    }
}