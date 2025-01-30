package ru.otus.interpreter.sql

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SqlSelectBuilder {
    private var table: String?  = null
    private val cols = mutableListOf<String>()
    private var where: SqlExpression? = null

    fun build() : String {
        if (table == null) throw IllegalArgumentException("No table")
        val cols = if (this.cols.isEmpty()) "*" else this.cols.joinToString(", ")
        val wherePart = if (where == null) "" else " where " + where!!.build()
        return "select $cols from $table$wherePart"
    }

    fun from(table: String) {
        this.table = table
    }

    fun select(vararg cols: String) {
        this.cols.addAll(cols)
    }

    fun where(expr: SqlExpression) {
        this.where = expr
    }
}

interface SqlExpression {
    fun build() : String
}

data class SqlCol(val name: String): SqlExpression {
    override fun build(): String  = name
}

data class SqlString(val value: String): SqlExpression {
    override fun build(): String  = "'$value'"
}

data class SqlEq(val left: SqlExpression, val right: SqlExpression): SqlExpression {
    override fun build(): String = left.build() + " = " + right.build()
}

infix fun String.eq(b: String): SqlExpression = SqlEq(SqlCol(this), SqlString(b))

fun query(block:
          SqlSelectBuilder.() -> Unit
): SqlSelectBuilder {
    val obj = SqlSelectBuilder()
    obj.block()
    return obj
}

class SqlDslUnitTest {
    private fun checkSQL(expected: String, sql: SqlSelectBuilder) {
        assertEquals(expected, sql.build())
    }


    @Test
    fun `simple select all from table`() {
        val expected = "select * from table"

        val real = query {
            from("table")
        }

        checkSQL(expected, real)
    }

    @Test
    fun `check that select can't be used without table`() {
        assertFailsWith<Exception> {
            query {
                select("col_a")
            }.build()
        }
    }

    @Test
    fun `select certain columns from table`() {
        val expected = "select col_a, col_b from table"

        val real = query {
            select("col_a", "col_b")
            from("table")
        }

        checkSQL(expected, real)
    }


    /**
     * __eq__ is "equals" function. Must be one of char:
     *  - for strings - "="
     *  - for numbers - "="
     *  - for null - "is"
     */
    @Test
    fun `select with complex where condition with one condition`() {
        val expected = "select * from table where col_a = 'id'"

        val real = query {
            from("table")
            where( "col_a" eq "id")
        }

        checkSQL(expected, real)
    }
/*
    /**
     * __nonEq__ is "non equals" function. Must be one of chars:
     *  - for strings - "!="
     *  - for numbers - "!="
     *  - for null - "!is"
     */
    @Test
    fun `select with complex where condition with two conditions`() {
        val expected = "select * from table where col_a != 0"

        val real = query {
            from("table")
            where("col_a" nonEq 0)
        }

        checkSQL(expected, real)
    }

    @Test
    fun `when 'or' conditions are specified then they are respected`() {
        val expected = "select * from table where (col_a = 4 or col_b !is null or col_c !is null)"

        val real = query {
            from("table")
            where(
                or(
                    "col_a" eq 4,
                    "col_b" nonEq null,
                    "col_c" nonEq null)
            )
        }

        checkSQL(expected, real)
    }
     */
}
