package ru.otus.proxy.practice.lazy

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

@Component
@org.springframework.context.annotation.Lazy
class HeavyClass {
    init {
        log.info { "Heavy class created" }
    }

    val property = 10

    fun method(a: Int) {
        println("$property, $a")
    }
}

@Component
class HeavyClassUser(@org.springframework.context.annotation.Lazy private val heavy: HeavyClass) {
    init {
        log.info { "Heavy class user created" }
    }

    fun f() {
        log.info { "f() called" }

        heavy.method(42)
    }
}

@SpringBootApplication
class Application

fun main(vararg args: String) {
    val ctx = runApplication<Application>(*args)
    val bean = ctx.getBean(HeavyClassUser::class.java)
    bean.f()
}