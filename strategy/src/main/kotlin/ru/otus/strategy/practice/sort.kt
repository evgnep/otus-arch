package ru.otus.strategy.practice

import mu.KotlinLogging
import java.util.Collections

private val log = KotlinLogging.logger {}

fun main() {
    val list = listOf(1, 42, -1, 5)
    println(list.sortedWith { a, b -> a.compareTo(b)})
    println(list.sortedWith { a, b -> -a.compareTo(b)})

    println(list.filter { it % 2 == 0 })
}