package ru.otus.proxy.practice

import mu.KotlinLogging
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

private val logger = KotlinLogging.logger {}


interface MyIntf {
    fun sum(a: Int, b: Int): Int
}

class MyClass : MyIntf {
    override fun sum(a: Int, b: Int): Int = a + b
}

class MyClassProxy(private val subject: MyIntf): MyIntf {
    override fun sum(a: Int, b: Int): Int {
        logger.info { "sum: $a, $b" }
        val res = subject.sum(a, b)
        logger.info { "res: $res" }
        return res
    }
}


fun main() {
    val myClass = MyClassProxy(MyClass())
    myClass.sum(2, 5)

//    val map = HashMap<String, Int>()
//    map["x"] = 42;
//    map["x"]
}