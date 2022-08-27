package ru.otus.cor.impl

import ru.otus.cor.ICorWorker

class CorWorker<T>(
    override val title: String,
    override val description: String = "",
    val blockOn: T.() -> Boolean = { true },
    val blockHandle: T.() -> Unit = {},
    val blockExcept: T.(Throwable) -> Unit = {},
) : ICorWorker<T> {
    override fun on(context: T): Boolean = blockOn(context)
    override fun handle(context: T) = blockHandle(context)
    override fun except(context: T, e: Throwable) = blockExcept(context, e)
}

