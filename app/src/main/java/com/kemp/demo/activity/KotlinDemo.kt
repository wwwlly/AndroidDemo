package com.kemp.demo.activity

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import com.kemp.demo.base.ShowTextActivity
import com.kemp.demo.kotlin.Circle
import com.kemp.demo.utils.DebugLog
import com.kemp.demo.utils.ProxyTest
import kotlinx.coroutines.*
import org.jetbrains.anko.button
import org.jetbrains.anko.editText
import org.jetbrains.anko.verticalLayout
import java.lang.Exception
import java.lang.Thread.sleep
import kotlin.properties.Delegates


const val TAG = "KotlinDemo"

/**
 * 测试kotlin
 */
class KotlinDemo : ShowTextActivity() {

    var i by Delegates.observable(1) { prop, old, new ->
        Log.d(TAG, "prop: $prop, old: $old, new: $new")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setText("KotlinDemo")
//        lifecycle

//        test1()
//        RetrofitHelper.test()
//        swap()
//        observe()
//        testConstructor()
//        test2()
//        testEquals()
//        testSynchrnoized()
//        testCoroutine()
//        testProxy()

        verticalLayout {
            val et = editText()
            button("aaa"){

            }
        }
    }

    /**
     * 函数类型
     * 函数类型可以有一个额外的接收者类型，它在表示法中的点之前指定： 类型 A.(B) -> C 表示可以在 A 的接收者对象上以一个 B 类型参数来调用并返回一个 C 类型值的函数。
     * 带有接收者的函数字面值通常与这些类型一起使用。
     *
     * 带有接收者的函数字面值
     */
    private fun test1() {
        val repeatFun: String.(Int) -> String = { times -> this.repeat(times) }
        val twoParameters: (String, Int) -> String = repeatFun // OK

        fun runTransformation(f: (String, Int) -> String): String {
            return f("hello", 3)
        }

        val result = runTransformation(repeatFun) // OK

        appendText("result = $result")
        println("result = $result")

        var fun1: (() -> Unit)? = null
        fun1 = { Log.d(TAG, "fun1 run") }
        fun1?.invoke()
//        fun1()
//        fun1
    }

    //<editor-fold desc="为什么使用契约">
    /**
     * 契约 contract()
     */
    private fun runFun(action: () -> Unit) {
        action()
    }

    fun getValue(): Int {
        var ret: Int
        runFun {
            ret = 15;
        }
//        return ret//编译报错
        return 0
    }
    //</editor-fold>

    /**
     * 交换两个变量
     */
    private fun swap() {
        var a = 1
        var b = 2

        Log.d(TAG, "a: $a, b: $b")

        a = b.also { b = a }

        Log.d(TAG, "a: $a, b: $b")
    }

    /**
     * 委托属性
     * 可观察属性
     */
    private fun observe() {
        i = 2
    }

    /**
     * 构造方法
     */
    private fun testConstructor() {
        Log.d(TAG, "==========testConstructor===========")
//        val shape = Shape()
//        val shape = Shape("Shape")
        val shape = Circle()

    }

    data class A(val name: String)

    /**
     * ?.
     * !!
     * ?:
     * String.toInt()
     */
    private fun test2() {
//        val name: String? = "www"
        val name: String? = null

//        if (name?.isNotEmpty()){ //等价于 !!.
//        if (name!!.isNotEmpty()){
//            Log.d(TAG, "name is not empty")
//        }

        val a: A? = null
        Log.d(TAG, "a's name is ${a?.name}")

        val msg = name ?: "name is not null"
        Log.d(TAG, msg)

//        val str: String? = "123"
        val str: String? = "www"
        val temp = try {
            str?.toInt()
        } catch (e: Exception) {
            Log.d(TAG, "str can't to Int")
            0
        } finally {
            0
        }
        Log.d(TAG, "str: $temp")
    }

    /**
     * equals()
     * ==
     * ===
     */
    private fun testEquals() {
        val a = "aaa"
        val b = "aaa"

        Log.d(TAG, "a.equals(b)=${a.equals(b)}")
        Log.d(TAG, "a == b =${a == b}")
        Log.d(TAG, "a === b =${a === (b)}")
    }

    /**
     * synchronized控制task1和task2执行完后才去执行task3
     *
     */
    fun testSynchrnoized() {
        val task1: () -> String = {
            sleep(2000)
            "Hello".also { println("task1 finished: $it") }
        }

        val task2: () -> String = {
            sleep(2000)
            "World".also { println("task2 finished: $it") }
        }

        val task3: (String, String) -> String = { p1, p2 ->
            sleep(2000)
            "$p1 $p2".also { println("task3 finished: $it") }
        }

        lateinit var s1: String
        lateinit var s2: String

        Thread {
            synchronized(Unit) {
                s1 = task1()
            }
        }.start()

        s2 = task2()

        //执行到这说明task2已执行完，由于匿名thread已获取Unit锁，等其释放（task1执行完）才会执行下面语句
        synchronized(Unit) {
            task3(s1, s2)
        }

    }

    /**
     * 协程
     */
    private fun testCoroutine() {
        val task1: () -> String = {
            sleep(2000)
            "Hello".also { DebugLog.d("task1 finished: $it") }
        }

        val task2: () -> String = {
            sleep(2000)
            "World".also { DebugLog.d("task2 finished: $it") }
        }

        val task3: (String, String) -> String = { p1, p2 ->
//            sleep(2000)
            "$p1 $p2".also { DebugLog.d("task3 finished: $it") }
        }

//        runBlocking {
//            val c1 = async(Dispatchers.IO) {
//                task1()
//            }
//
//            val c2 = async(Dispatchers.IO) {
//                task2()
//            }
//
//            task3(c1.await(), c2.await())
//        }

        GlobalScope.launch {
            val c1 = async(Dispatchers.IO) {
                task1()
            }

            val c2 = async(Dispatchers.IO) {
                task2()
            }

            task3(c1.await(), c2.await())
        }
    }

    /**
     * 防止被抓包
     */
    private fun testProxy() {
        val proxyTest = ProxyTest(this)

//        proxyTest.testIsWifiProxy()
//        proxyTest.testRequestHttpURLConnection()
        proxyTest.testRequestOkHttp()
    }

}