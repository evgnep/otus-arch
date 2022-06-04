package ru.otus.proxy.prepare

import mu.KotlinLogging

private val log = KotlinLogging.logger {}

interface Matrix {
    operator fun get(col: Int, row: Int): Int
    operator fun set(col: Int, row: Int, value: Int)

    val width: Int
    val height: Int
}

class MatrixImpl(
    override val width: Int,
    override val height: Int
) : Matrix {
    init {
        log.info { "Matrix created" }
    }

    private val data: List<MutableList<Int>> = buildList(height){
        for (y in 0 until height) {
            val row = mutableListOf<Int>()
            for (x in 0 until width)
                row.add(0)
            add(row)
        }
    }

    override fun get(col: Int, row: Int): Int {
        return data[row][col]
    }

    override fun set(col: Int, row: Int, value: Int) {
        data[row][col] = value
    }
}

class MatrixSum(args: List<Matrix>) : Matrix {
    private val args : List<Matrix> = buildList {
        for (a in args) {
            when(a) {
                is MatrixSum -> addAll(a.args)
                else -> add(a)
            }
        }
    }

    private val result = lazy {
        val res = MatrixImpl(width, height)
        for (col in 0 until width)
            for (row in 0 until height) {
                var value = 0
                for (m in this.args)
                    value += m[col, row]
                res[col, row] = value
            }
        res
    }

    override val width: Int = this.args[0].width
    override val height: Int = this.args[0].height

    override fun get(col: Int, row: Int): Int  = result.value[col, row]

    override fun set(col: Int, row: Int, value: Int) {
        result.value[col, row] = value
    }
}

operator fun Matrix.plus(b: Matrix) : Matrix = MatrixSum(listOf(this, b))
/*
{
    val res = MatrixImpl(width, height)
    for (col in 0 until width)
        for (row in 0 until height)
            res[col, row] = this[col, row] + b[col, row]
    return res
}*/

fun main() {
    val res = MatrixImpl(2, 2) + MatrixImpl(2, 2) + MatrixImpl(2, 2) + MatrixImpl(2, 2)
    res[0, 0]
}