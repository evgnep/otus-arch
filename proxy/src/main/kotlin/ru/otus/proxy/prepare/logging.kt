package ru.otus.proxy.prepare

import mu.KotlinLogging
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

private val logger = KotlinLogging.logger {}

class DynamicInvocationHandler(private val subject: Any) : InvocationHandler {
    override operator fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
        logger.info { "Invoked method: ${method.name}: ${args.contentToString()}" }

        try {
            val res : Any? = method.invoke(subject, *args)
            logger.info { "method result: $res" }
            return res
        } catch (e: Throwable) {
            logger.error(e) { "method exception" }
            throw e
        }
    }

    companion object {
        inline fun <reified T> createProxy(subject: Any) : T {
            return Proxy.newProxyInstance(T::class.java.classLoader, arrayOf(T::class.java), DynamicInvocationHandler(subject)) as T
        }
    }
}

interface MyIntf {
    fun sum(a: Int, b: Int): Int
}

class MyClass : MyIntf {
    override fun sum(a: Int, b: Int): Int = a + b
}

class MyClassProxy(private val subject: ru.otus.proxy.practice.MyIntf): ru.otus.proxy.practice.MyIntf {
    override fun sum(a: Int, b: Int): Int {
        logger.info { "sum: $a, $b" }
        val res = subject.sum(a, b)
        logger.info { "res: $res" }
        return res
    }
}



fun main() {
    val map = DynamicInvocationHandler.createProxy<MutableMap<String, Int>>(HashMap<String, Int>())
    map["x"] = 42;
    map["x"]

    val myClass = DynamicInvocationHandler.createProxy<MyIntf>(MyClass())
    myClass.sum(2, 5)
}