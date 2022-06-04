package ru.otus.proxy.practice

import mu.KotlinLogging

private val log = KotlinLogging.logger {}

private val list: MutableList<Int> = mutableListOf()

//sCollections.synchronizedList

fun threadFun() {
    for (i in 0..999) {
        list.add(i)
    }
}

fun main(args: Array<String>) {
    val threads = listOf(Thread(::threadFun), Thread(::threadFun))
    for (t in threads) t.start()
    for (t in threads) t.join()
    log.info("a={}", list.size)
}