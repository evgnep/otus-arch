package ru.otus.proxy.practice

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

private fun someFunc() {
    val con = DbConnection("con1")
    con.close()

}

fun main() {
    try {
        someFunc()
    } catch (e: Exception) {
        logger.warn { "Exception!" }
    }
}