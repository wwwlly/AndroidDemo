package com.kemp.demo.kotlin

import android.util.Log

/**
 * 构造函数
 *
 * 默认情况下，Kotlin 类是最终（final）的：它们不能被继承。 要使一个类可继承，请用 open 关键字标记它。
 * 在 Kotlin 中的一个类可以有一个主构造函数以及一个或多个次构造函数。
 * 如果类有一个主构造函数，每个次构造函数需要委托给主构造函数， 可以直接委托或者通过别的次构造函数间接委托。
 *
 * 执行顺序：
 * 1.先执行父类的再执行子类的；
 * 2.先执行属性赋值和init块（同时存在顺序执行）然后执行构造方法。
 */

const val TAG = "Constructors"

private fun log(msg: String) {
    Log.d(TAG, msg)
}

open class Shape(name: String) {

    private val firstProperty = "First property: $name".also(::log)

    init {
        log("First initializer block that prints $name")
    }

    val secondProperty = "Second property: ${name.length}".also(::log)

    init {
        log("Second initializer block that prints ${name.length}")
    }

    constructor() : this("Shape") {
        log("Shape's constructor() init")
    }

    open fun getText(): String {
        return "www"
    }
}

class Circle(val id: Int = 1, name: String) : Shape() {

    val firstProperty = "First property: $name".also(::log)

    init {
        log("Circle Init block")
    }

    constructor() : this(2, "Circle") {
        log("Circle's constructor(id, name) init")
    }

    override fun getText(): String {
        return super.getText()
    }

    fun Test(){

    }
}