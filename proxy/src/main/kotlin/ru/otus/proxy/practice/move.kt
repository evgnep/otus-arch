package ru.otus.proxy.practice


interface Command {
    fun execute()
}

interface IMovableObject {
    var coordinate: Int
    val velocity: Int
}

class MoveCommand(private val movableObject: IMovableObject) : Command {
    override fun execute() {
        var coordinate = movableObject.coordinate
        val velocity = movableObject.velocity

        coordinate += velocity

        movableObject.coordinate = coordinate
    }
}
