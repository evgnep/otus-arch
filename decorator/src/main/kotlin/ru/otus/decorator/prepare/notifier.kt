package ru.otus.decorator.prepare

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
        log.info { "Sens sms $message to $phone" }
        super.notify(message)
    }
}

class EmailNotifier(val email: String, base: Notifier?) : NotifierDecorator(base) {
    override fun notify(message: String) {
        log.info { "Sens mail $message to $email" }
        super.notify(message)
    }
}

fun doSomething(notifier: Notifier) {
    notifier.notify("Hello")
}

fun main() {
    val notifier = EmailNotifier("a@b.c", SmsNotifier("1234", null))
}

