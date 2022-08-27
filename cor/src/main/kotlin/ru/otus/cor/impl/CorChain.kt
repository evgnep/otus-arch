package ru.otus.cor.impl

import ru.otus.cor.ICorExec
import ru.otus.cor.ICorWorker

/**
 * Реализация цепочки (chain), которая исполняет свои вложенные цепочки и рабочие
 */
class CorChain<T>(
    private val execs: List<ICorExec<T>>,
    override val title: String,
    override val description: String = "",
    private val blockOn: T.() -> Boolean = { true },
    private val blockExcept: T.(Throwable) -> Unit = {},
) : ICorWorker<T> {
    override fun on(context: T): Boolean = blockOn(context)
    override fun except(context: T, e: Throwable) = blockExcept(context, e)
    override fun handle(context: T) {
        execs.forEach {
            it.exec(context)
        }
    }
}