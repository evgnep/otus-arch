package ru.otus.proxy.practice.cache

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.EnableCaching
import org.springframework.stereotype.Component


private val log = KotlinLogging.logger {}

@Component
class Evaluator {
    @Cacheable("cache")
    fun sum(a: Int, b: Int): Int {
        log.info { "... sum called: $a + $b" }
        return a + b
    }
}

@SpringBootApplication
@EnableCaching
class Application

fun main(vararg args: String) {
    val ctx = runApplication<Application>(*args)
    val bean = ctx.getBean(Evaluator::class.java)

    log.info("{}", bean::class.java)

    log.info("1. {}", bean.sum(1, 2))
    log.info("2. {}", bean.sum(1, 2))
    log.info("3. {}", bean.sum(1, 2))
    log.info("4. {}", bean.sum(1, 3))
    log.info("5. {}", bean.sum(1, 2))
}