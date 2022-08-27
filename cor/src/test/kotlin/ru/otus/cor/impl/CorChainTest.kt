package ru.otus.cor.impl

import org.junit.jupiter.api.Test
import ru.otus.cor.CorStatuses
import ru.otus.cor.ICorChainDsl
import ru.otus.cor.TestContext
import ru.otus.cor.chain
import ru.otus.cor.worker

internal class CorChainTest {
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