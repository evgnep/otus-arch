package ru.otus.interpreter.dsl

import ru.otus.interpreter.AbstractExpression
import ru.otus.interpreter.Const
import ru.otus.interpreter.Divide
import ru.otus.interpreter.Minus
import ru.otus.interpreter.Multiply
import ru.otus.interpreter.Plus
import ru.otus.interpreter.Variable

operator fun String.unaryPlus() = Variable(this)

operator fun AbstractExpression.minus(b: String) = Minus(this, Variable(b))
operator fun AbstractExpression.plus(b: String) = Plus(this, Variable(b))
operator fun AbstractExpression.times(b: String) = Multiply(this, Variable(b))
operator fun AbstractExpression.div(b: String) = Divide(this, Variable(b))

operator fun AbstractExpression.minus(b: Double) = Minus(this, Const(b))
operator fun AbstractExpression.plus(b: Double) = Plus(this, Const(b))
operator fun AbstractExpression.times(b: Double) = Multiply(this, Const(b))
operator fun AbstractExpression.div(b: Double) = Divide(this, Const(b))

operator fun AbstractExpression.minus(b: AbstractExpression) = Minus(this, b)
operator fun AbstractExpression.plus(b: AbstractExpression) = Plus(this, b)
operator fun AbstractExpression.times(b: AbstractExpression) = Multiply(this, b)
operator fun AbstractExpression.div(b: AbstractExpression) = Divide(this, b)

fun main() {
    //val expr = (+"A" - "B") * "C" + 1.0
    //val expr = (+"A" - "B") * "C" + ("B" + 1.0)
    val expr = (+"A" - "B") * "C" + (+"B" + 1.0)
    println(expr.interpret(mapOf("A" to 10.0, "B" to 1.0, "C" to 3.0)))
}