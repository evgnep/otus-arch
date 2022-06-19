package ru.otus.interpreter.dsl2

import ru.otus.interpreter.AbstractExpression
import ru.otus.interpreter.Const
import ru.otus.interpreter.Divide
import ru.otus.interpreter.Minus
import ru.otus.interpreter.Multiply
import ru.otus.interpreter.Plus
import ru.otus.interpreter.Variable

fun expr(a: String) = Variable(a)
fun expr(a: Double) = Const(a)

operator fun AbstractExpression.minus(b: AbstractExpression) = Minus(this, b)
operator fun AbstractExpression.plus(b: AbstractExpression) = Plus(this, b)
operator fun AbstractExpression.times(b: AbstractExpression) = Multiply(this, b)
operator fun AbstractExpression.div(b: AbstractExpression) = Divide(this, b)



fun main() {
    val expr = (expr("A") - expr("B")) * expr("C") + expr(1.0)

    println(expr.interpret(mapOf("A" to 10.0, "B" to 1.0, "C" to 3.0)))
}