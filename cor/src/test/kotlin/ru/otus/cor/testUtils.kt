package ru.otus.cor

data class TestContext(
    var status: CorStatuses = CorStatuses.NONE,
    var some: Int = 0,
    var history: String = "",
)

enum class CorStatuses {
    NONE,
    RUNNING,
    FAILING,
    DONE,
    ERROR
}