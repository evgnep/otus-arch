package ru.otus.bus

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component


private val log = KotlinLogging.logger {}

class CustomEvent(val source: SomeObject) : ApplicationEvent(source)

@Component
class SomeObject(private val publisher: ApplicationEventPublisher) {
    var state = 0

    fun doSomething() {
        state += 1
        log.info { "doSomething: $state" }
        publisher.publishEvent(CustomEvent(this))
    }
}

@Component
class Listener {
    @EventListener
    fun handleContextStart(cse: CustomEvent) {
        log.info { "SubscriberA: ${cse.source.state}" }
    }
}

@SpringBootApplication
class Application

fun main() {
    val ctx = runApplication<Application>()
    ctx.getBean(SomeObject::class.java).doSomething()
}