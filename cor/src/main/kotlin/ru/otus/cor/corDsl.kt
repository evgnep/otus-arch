package ru.otus.cor

import ru.otus.cor.impl.CorChainDsl
import ru.otus.cor.impl.CorWorkerDsl

@DslMarker
annotation class CorDslMarker

/**
 * Базовый билдер (dsl)
 */
@CorDslMarker
interface ICorExecDsl<T> {
    var title: String
    var description: String
    fun on(function: T.() -> Boolean)
    fun except(function: T.(e: Throwable) -> Unit)

    fun build(): ICorExec<T>
}

/**
 * Билдер (dsl) для цепочек (chain)
 */
@CorDslMarker
interface ICorChainDsl<T> : ICorExecDsl<T> {
    fun add(worker: ICorExecDsl<T>)
}

/**
 * Билдер (dsl) для рабочих (worker)
 */
@CorDslMarker
interface ICorWorkerDsl<T> : ICorExecDsl<T> {
    fun handle(function: T.() -> Unit)
}

/**
 * Точка входа в dsl построения цепочек. Создает корневую цепочку.
 *
 * Пример:
 * ```
 *  chain<SomeContext> {
 *      worker {
 *      }
 *      chain {
 *          worker(...) {
 *          }
 *          worker(...) {
 *          }
 *      }
 *      parallel {
 *         ...
 *      }
 *  }
 * ```
 */
fun <T> chain(function: ICorChainDsl<T>.() -> Unit): ICorChainDsl<T> = CorChainDsl<T>().apply(function)


/**
 * Создает цепочку внутри цепочки
 */
fun <T> ICorChainDsl<T>.chain(function: ICorChainDsl<T>.() -> Unit) {
    add(CorChainDsl<T>().apply(function))
}

/**
 * Создает рабочего внутри цепочки
 */
fun <T> ICorChainDsl<T>.worker(function: ICorWorkerDsl<T>.() -> Unit) {
    add(CorWorkerDsl<T>().apply(function))
}

/**
 * Создает рабочего внутри цепочки с on и except по умолчанию
 */
fun <T> ICorChainDsl<T>.worker(
    title: String,
    description: String = "",
    blockHandle: T.() -> Unit
) {
    add(CorWorkerDsl(title = title, description = description, blockHandle = blockHandle))
}