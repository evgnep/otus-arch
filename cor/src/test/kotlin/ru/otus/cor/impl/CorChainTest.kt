package ru.otus.cor.impl

import org.junit.jupiter.api.Test
import ru.otus.cor.CorStatuses
import ru.otus.cor.ICorChainDsl
import ru.otus.cor.TestContext
import ru.otus.cor.chain
import ru.otus.cor.worker
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class CorChainTest {

    @Test
    fun `worker should execute handle`() {
        val worker = CorWorker<TestContext>(
            title = "w1",
            blockHandle = { history += "w1; " }
        )
        val ctx = TestContext()
        worker.exec(ctx)
        assertEquals("w1; ", ctx.history)
    }

    @Test
    fun `worker should not execute when off`() {
        val worker = CorWorker<TestContext>(
            title = "w1",
            blockOn = { status == CorStatuses.ERROR },
            blockHandle = { history += "w1; " }
        )
        val ctx = TestContext()
        worker.exec(ctx)
        assertEquals("", ctx.history)
    }

    @Test
    fun `worker should handle exception`() {
        val worker = CorWorker<TestContext>(
            title = "w1",
            blockHandle = { throw RuntimeException("some error") },
            blockExcept = { e -> history += e.message }
        )
        val ctx = TestContext()
        worker.exec(ctx)
        assertEquals("some error", ctx.history)
    }

    @Test
    fun `chain should execute workers`() {
        val createWorker = { title: String ->
            CorWorker<TestContext>(
                title = title,
                blockOn = { status == CorStatuses.NONE },
                blockHandle = { history += "$title; " }
            )
        }
        val chain = CorChain(
            execs = listOf(createWorker("w1"), createWorker("w2")),
            title = "chain",
        )
        val ctx = TestContext()
        chain.exec(ctx)
        assertEquals("w1; w2; ", ctx.history)
    }

    private fun execute(dsl: ICorChainDsl<TestContext>): TestContext {
        val ctx = TestContext()
        dsl.build().exec(ctx)
        return ctx
    }

    @Test
    fun `handle should execute`() {
        assertEquals("w1; ", execute(chain {
            worker {
                handle { history += "w1; " }
            }
        }).history)
    }

    @Test
    fun `on should check condition`() {
        assertEquals("w2; w3; ", execute(chain {
            worker {
                on { status == CorStatuses.ERROR }
                handle { history += "w1; " }
            }
            worker {
                on { status == CorStatuses.NONE }
                handle {
                    history += "w2; "
                    status = CorStatuses.FAILING
                }
            }
            worker {
                on { status == CorStatuses.FAILING }
                handle { history += "w3; " }
            }
        }).history)
    }

    @Test
    fun `except should execute when exception`() {
        assertEquals("some error", execute(chain {
            worker {
                handle { throw RuntimeException("some error") }
                except { history += it.message }
            }
        }).history)
    }

    @Test
    fun `should throw when exception and no except`() {
        assertFails {
            execute(chain {
                worker("throw") { throw RuntimeException("some error") }
            })
        }
    }


    @Test
    fun `create and run chain`() {
        val chain = CorChain<TestContext>(
            title = "my first chain",
            execs = listOf(
                CorWorker(
                    title = "w1",
                    blockOn = { status == CorStatuses.NONE },
                    blockHandle = {
                        status = CorStatuses.RUNNING
                        history += "w1; "
                    }
                ),
                CorChain(
                    title = "inner chain",
                    execs = listOf(
                        someComplexWorker(),
                        CorWorker(
                            title = "w3",
                            blockOn = { status == CorStatuses.ERROR },
                            blockHandle = {
                                history += "w3; "
                            }
                        ),
                    )
                )
            )
        )

        val ctx = TestContext()
        chain.exec(ctx)
        println(ctx)
    }

    private fun someComplexWorker() = CorWorker<TestContext>(
        title = "w2",
        blockHandle = {
            history += "w2; "
        }
    )

    @Test
    fun `create and run chain with dsl`() {
        val chain = chain<TestContext> {
            title = "my first chain"

            worker {
                title = "w1"
                on { status == CorStatuses.NONE }
                handle {
                    status = CorStatuses.RUNNING
                    history += "w1; "
                }
            }

            chain {
                title = "inner chain"

                someOtherComplexWorker()

                worker {
                    title = "w3"
                    on { status == CorStatuses.ERROR }
                    handle {
                        history += "w3; "
                    }
                }

            }
        }.build()

        val ctx = TestContext()
        chain.exec(ctx)
        println(ctx)
    }

    private fun ICorChainDsl<TestContext>.someOtherComplexWorker() = worker("w2") {
        history += "w2; "
    }
}