package ru.otus.visitor.prepare

interface GameObject {
    fun visit(visitor: GameObjectVisitor)
}

interface GameObjectVisitor {
}

interface TankVisitor: GameObjectVisitor {
    fun visit(tank: Tank)
}

interface DotVisitor: GameObjectVisitor {
    fun visit(dot: Dot)
}

interface SoldierVisitor: GameObjectVisitor {
    fun visit(soldier: Soldier)
}

class Tank(val damage: Int, val mileage: Int): GameObject {
    override fun visit(visitor: GameObjectVisitor) {
        if (visitor is TankVisitor)
            visitor.visit(this)
    }
}

class Soldier(val damage: Int, val mileage: Int): GameObject {
    override fun visit(visitor: GameObjectVisitor) {
        if (visitor is SoldierVisitor)
            visitor.visit(this)
    }
}

class Dot(val damage: Int): GameObject {
    override fun visit(visitor: GameObjectVisitor) {
        if (visitor is DotVisitor)
            visitor.visit(this)
    }
}

fun main() {
    val objs = listOf(Tank(10, 100), Tank(15, 90), Dot(0), Dot(80), Soldier(0, 5))

    val maxDamage = object : TankVisitor, DotVisitor, SoldierVisitor {
        var max = 0
        override fun visit(tank: Tank) {
            max = Math.max(max, tank.damage)
        }

        override fun visit(dot: Dot) {
            max = Math.max(max, dot.damage)
        }

        override fun visit(soldier: Soldier) {
            max = Math.max(max, soldier.damage)
        }
    }
    objs.forEach{ it.visit(maxDamage) }
}
