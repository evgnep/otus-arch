package ru.otus.cor.impl

import ru.otus.cor.ICorChainDsl
import ru.otus.cor.ICorExec
import ru.otus.cor.ICorExecDsl

class CorChainDsl<T> : ICorChainDsl<T> {
    private val workers: MutableList<ICorExecDsl<T>> = mutableListOf()
    private var blockOn: T.() -> Boolean = { true }
    private var blockExcept: T.(e: Throwable) -> Unit = { e: Throwable -> throw e }

    override var title: String = ""
    override var description: String = ""

    override fun add(worker: ICorExecDsl<T>) {
        workers.add(worker)
    }

    override fun on(function: T.() -> Boolean) {
        blockOn = function
    }

    override fun except(function: T.(e: Throwable) -> Unit) {
        blockExcept = function
    }

    override fun build(): ICorExec<T> = CorChain(
        title = title,
        description = description,
        execs = workers.map { it.build() },
        blockOn = blockOn,
        blockExcept = blockExcept
    )
}