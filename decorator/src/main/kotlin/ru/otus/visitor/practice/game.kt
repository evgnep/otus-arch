package ru.otus.visitor.practice

interface GameObject {
    fun visit(visitor: GameObjectVisitor)
}

interface GameObjectVisitor

interface TankVisitor: GameObjectVisitor {
    fun visitTank(tank: Tank)
}

interface DotVisitor: GameObjectVisitor {
    fun visitDot(dot: Dot)
}

interface SoldierVisitor: GameObjectVisitor {
    fun visitSoldier(soldier: Soldier)
}

class Tank(val damage: Int, val mileage: Int): GameObject {
    override fun visit(visitor: GameObjectVisitor) {
        if (visitor is TankVisitor)
            visitor.visitTank(this)
    }
}

class Dot(val damage: Int): GameObject {
    override fun visit(visitor: GameObjectVisitor) {
        if (visitor is DotVisitor)
            visitor.visitDot(this)
    }
}

class Soldier(val damage: Int): GameObject {
    override fun visit(visitor: GameObjectVisitor) {
        if (visitor is SoldierVisitor)
            visitor.visitSoldier(this)
    }
}

class DamageFinder: TankVisitor, DotVisitor {
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
