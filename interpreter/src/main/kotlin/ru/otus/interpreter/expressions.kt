package ru.otus.interpreter

typealias Context = Map<String, Double>

interface AbstractExpression {
    fun interpret(ctx: Context) : Double
}

data class Const(val value: Double): AbstractExpression {
    override fun interpret(ctx: Context): Double = value
}

data class Variable(val name: String): AbstractExpression {
    override fun interpret(ctx: Context): Double = ctx[name] ?: throw IllegalArgumentException("Unknown variable $name")
}

data class Plus(val a: AbstractExpression, val b: AbstractExpression): AbstractExpression {
    override fun interpret(ctx: Context): Double = a.interpret(ctx) + b.interpret(ctx)
}

data class Minus(val a: AbstractExpression, val b: AbstractExpression): AbstractExpression {
    override fun interpret(ctx: Context): Double = a.interpret(ctx) - b.interpret(ctx)
}

data class Multiply(val a: AbstractExpression, val b: AbstractExpression): AbstractExpression {
    override fun interpret(ctx: Context): Double = a.interpret(ctx) * b.interpret(ctx)
}

data class Divide(val a: AbstractExpression, val b: AbstractExpression): AbstractExpression {
    override fun interpret(ctx: Context): Double = a.interpret(ctx) / b.interpret(ctx)
}

fun main() {
    val expr = Multiply(Minus(Variable("A"), Variable("B")), Variable("C"))
    println(expr.interpret(mapOf("A" to 10.0, "B" to 1.0, "C" to 3.0)))
}