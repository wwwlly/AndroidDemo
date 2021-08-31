package com.kemp.demo.activity

import android.os.Bundle
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
import com.tencent.mmkv.MMKV

@Description("测试")
class TestDemo : ShowTextActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        TestSort.test()
//        TestTime.testCurrentTimeMillis()
//        TestTime.getTimeZoneInfo()
//        testTime()
        testCommonLib()
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
}