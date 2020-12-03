package com.kemp.demo.activity

import android.os.Bundle
import android.util.Log
import com.kemp.demo.base.ShowTextActivity
import com.kemp.demo.kotlin.Circle
import com.kemp.demo.kotlin.RetrofitHelper
import com.kemp.demo.kotlin.Shape
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

        test1()
        RetrofitHelper.test()
        swap()
        observe()
        testConstructor()
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

}