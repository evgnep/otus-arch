package ru.otus.visitor.practice

interface GameObject {
    fun visit(visitor: GameObjectVisitor)
}

interface GameObjectVisitor {
    fun visitTank(tank: Tank) {}
    fun visitDot(dot: Dot) {}

    // fun visitSoldier(soldier: Soldier) {}
}
class Tank(val damage: Int, val mileage: Int): GameObject {
    override fun visit(visitor: GameObjectVisitor) {
        visitor.visitTank(this)
    }
}

class Dot(val damage: Int): GameObject {
    override fun visit(visitor: GameObjectVisitor) {
        visitor.visitDot(this)
    }
}

//class Soldier(val damage: Int): GameObject {
//    override fun visit(visitor: GameObjectVisitor) {
//        visitor.visitSoldier(this)
//    }
//}

class DamageFinder: GameObjectVisitor {
    var max = 0
    override fun visitTank(tank: Tank) {
        max = Math.max(max, tank.damage)
    }

    override fun visitDot(dot: Dot) {
        max = Math.max(max, dot.damage)
    }
}

fun main() {
    val objs = listOf(Tank(10, 100), Tank(15, 90), Dot(0), Dot(80))
    val x: Iterable<Int>


    val maxDamage = DamageFinder()
    objs.forEach{ it.visit(maxDamage) }
    println("max damage: ${maxDamage.max}")
}
