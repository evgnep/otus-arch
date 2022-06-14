package ru.otus.decorator.practice

import mu.KotlinLogging

private val log = KotlinLogging.logger {}

interface Notifier {
    fun notify(message: String)
}

open class NotifierDecorator(val base: Notifier?) : Notifier {
    override fun notify(message: String) {
        base?.notify(message)
    }
}

class SmsNotifier(val phone: String, base: Notifier?) : NotifierDecorator(base) {
    override fun notify(message: String) {
        super.notify(message)
        log.info { "Send sms $message to $phone" }
    }
}

class EmailNotifier(val email: String, base: Notifier?) : NotifierDecorator(base) {
    override fun notify(message: String) {
        super.notify(message)
        log.info { "Send mail $message to $email" }
    }
}

class TelegramNotifier(val id: String, base: Notifier?) : NotifierDecorator(base) {
    override fun notify(message: String) {
        super.notify(message)
        log.info { "Send telegram $message to $id" }
    }
}

class ListNotifier(val notifiers: List<Notifier>): Notifier {
    override fun notify(message: String) {
        notifiers.forEach { it.notify(message) }
    }
}

fun doSomething(notifier: Notifier) {
    notifier.notify("Hello")
}

fun main() {
    val notifier = SmsNotifier("12345", EmailNotifier("a@b.c", null))
    doSomething(notifier)
}
