package com.kemp.demo.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.pm.ProviderInfo
import android.database.Cursor
import android.net.Uri
import android.webkit.MimeTypeMap
import com.kemp.demo.utils.DebugLog

class MyFileProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        DebugLog.d("onCreate")
        return true
    }

    override fun attachInfo(context: Context?, info: ProviderInfo?) {
        super.attachInfo(context, info)
        DebugLog.d("attachInfo")
        DebugLog.d("info : authority: ${info?.authority}, exported: ${info?.exported}, grantUriPermissions: ${info?.grantUriPermissions}")
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        DebugLog.d("query")
        return null
    }

    override fun getType(uri: Uri): String? {
        DebugLog.d("getType")
        val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension("apk")
        return if (mime?.isEmpty() != false) mime else "application/octet-stream"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        DebugLog.d("insert")
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        DebugLog.d("delete")
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        DebugLog.d("update")
        return 0
    }
}