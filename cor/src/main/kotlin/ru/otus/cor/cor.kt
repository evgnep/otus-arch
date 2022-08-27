package ru.otus.cor

/**
 * Блок кода, который обрабатывает контекст. Имеет имя и описание
 */
interface ICorExec<T> {
    val title: String
    val description: String
    fun exec(context: T)
}

/**
 * Реализация ICorExec, выполняется по условию и умеет обрабатывать свои исключения
 */
interface ICorWorker<T> : ICorExec<T> {
    fun on(context: T): Boolean
    fun handle(context: T)
    fun except(context: T, e: Throwable)

    override fun exec(context: T) {
        if (on(context)) {
            try {
                handle(context)
            } catch (e: Throwable) {
                except(context, e)
            }
        }
    }
}
