package com.kemp.demo.activity

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.kemp.annotations.Description
import com.kemp.commonlib.piece.Piece
import com.kemp.commonlib.tools.invoke
import com.kemp.commonlib.util.GsonUtils
import com.kemp.demo.arithmetic.TestSort
import com.kemp.demo.base.BaseActivity
import com.kemp.demo.base.ShowTextActivity
import com.kemp.demo.model.Person
import com.kemp.demo.model.User
import com.kemp.demo.utils.DebugLog
import com.kemp.demo.utils.TestTime
import com.kemp.demo.utils.TimeUtils
import com.kemp.demo.utils.ToolUtils
import com.tencent.mmkv.MMKV
import java.io.File

@Description("测试")
class TestDemo : ShowTextActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        TestSort.test()
//        TestTime.testCurrentTimeMillis()
//        TestTime.getTimeZoneInfo()
//        testTime()
//        testCommonLib()
        testFile()

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//            != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100);
//        } else {
//            testMediaPlay()
//        }

        val currentProcessName = ToolUtils.getCurrentProcessName(this)
        setText("当前进程：$currentProcessName")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                testMediaPlay()
            }
        }
    }

    private fun testTime() {
        setText("testTime")
        val currentTime = System.currentTimeMillis()
        appendText("currentTime: $currentTime")
        appendText("currentTime: ${TimeUtils.format(currentTime)}")
    }

    private fun testCommonLib() {
        val str: String = true("true")("false")
        DebugLog.d("str: $str")

        MMKV.initialize(this)
        val user = User("www", "123")
        DebugLog.d("user json: ${GsonUtils.toGson(user)}")
        MMKV.mmkvWithID("piece").encode("user", GsonUtils.toGson(user))
        val userResult = Piece.get<User>()
        DebugLog.d("userResult: ${userResult?.firstName} , ${userResult?.lastName}")

        val personList = arrayListOf<Person>()
        for (i in 0..2) {
            personList.add(Person("www$i"))
        }
        DebugLog.d("person list json: ${GsonUtils.toGson(personList)}")
        MMKV.mmkvWithID("piece").encode("person_list", GsonUtils.toGson(personList))
        val personListJson = MMKV.mmkvWithID("piece").decodeString("person_list", "")
        DebugLog.d("personListJson: $personListJson")
        val personListResult = Piece.getCollection<List<Person>>()
        if (personListResult != null) {
            DebugLog.d("person list Result is not null")
            for (person in personListResult) {
                DebugLog.d("person Result: $person")
            }
        } else {
            DebugLog.d("person list Result is null")
        }
    }

    private fun testFile() {
        val path = Environment.getExternalStorageDirectory().absolutePath
        DebugLog.d("path: $path")
//        val rootFile = File(path)
        val rootFile = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        DebugLog.d("root file: ${rootFile?.absolutePath ?: "null"}")
        for (file: File in rootFile?.listFiles()!!) {
            DebugLog.d("file: ${file.name}")
        }
    }

    private fun testMediaPlay() {
        val path = Environment.getExternalStorageDirectory().absolutePath;
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource("$path/Music/我是愤怒.mp3")
        mediaPlayer.setOnPreparedListener {
            it.start()
        }
        mediaPlayer.prepare()
    }
}