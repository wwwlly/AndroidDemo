package com.kemp.demo.activity

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.kemp.demo.R
import com.kemp.demo.utils.DebugLog

class ProviderDemo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider)


    }

    fun testInsert(view: View) {
        val uri = Uri.parse("content://com.kemp.demo.authMyFileProvider/tablename")

        val contentValue = ContentValues().apply {
            put("name", "zhangsan")
            put("age", 22)
        }
        contentResolver.insert(uri, contentValue)
    }

    fun testGetType(view: View) {
        val uri = Uri.parse("content://com.kemp.demo.authMyFileProvider/tablename")

        val type = contentResolver.getType(uri)
        DebugLog.d("type: $type")
    }
}