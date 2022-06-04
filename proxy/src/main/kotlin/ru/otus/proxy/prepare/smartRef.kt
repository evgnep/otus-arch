package ru.otus.proxy.prepare

import mu.KotlinLogging
import java.io.Closeable

private val logger = KotlinLogging.logger {}

class DbConnection(val name: String) {
    init {
        logger.info { "Created connection $name" }
    }

    fun someMethod() {}

    fun close() {
        logger.info { "Closed connection $name" }
    }
}

fun someFunc() {
    val con = DbConnection("con1")
    try {
        val con2 = DbConnection("con2")
        try {
            listOf(1)[1]
            //con.close()
        } finally {
            con2.close()
        }
    } finally {
        con.close()
    }

}

class ClosableProxy<T>(val subject: T, private val closeFunction: T.() -> Unit): Closeable {
    override fun close() {
        subject.closeFunction()
    }
}

fun someFunc2() {
    ClosableProxy(DbConnection("con1")){ close() }.use { con1 ->
        ClosableProxy(DbConnection("con2")){ close() }.use { con2 ->
            con1.subject.someMethod()
            listOf(1)[1]
            con2.subject.someMethod()
        }
    }
}

fun main() {
    try {
        someFunc2()
    } catch (e: Exception) {
        logger.warn { "Exception!" }
    }
}