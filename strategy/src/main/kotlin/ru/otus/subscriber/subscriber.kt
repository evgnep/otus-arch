package ru.otus.subscriber

import mu.KotlinLogging

private val log = KotlinLogging.logger {}

typealias Observer = (SomeObject)->Unit

class SomeObject {
    private val observers = mutableListOf<Observer>()
    var state = 0

    fun attach(observer: Observer) {
        observers.add(observer)
    }

    fun doSomething() {
        state += 1
        log.info { "doSomething: $state" }
        observers.forEach{ it(this) }
    }
}

fun main() {
    val obj = SomeObject()

    obj.doSomething()

    obj.attach {
        log.info { "SubscriberA: ${it.state}" }
        //it.attach { log.info { "SubscriberInner: ${it.state}" } }
    }

    obj.doSomething()
}