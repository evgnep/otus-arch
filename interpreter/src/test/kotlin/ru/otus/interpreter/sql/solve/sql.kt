package ru.otus.interpreter.sql.solve

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SqlSelectBuilder {
    private var table: String? = null
    private var columns = mutableListOf<String>()
    private var where: SqlExpression? = null

    fun build(): String {
        requireNotNull(table) { "table must be set" }
        val cols = if (columns.isEmpty()) "*" else columns.joinToString(", ")
        val wherePart = if (where == null) "" else " where " + where!!.build()
        return "select $cols from $table$wherePart"
    }

    fun from(table: String) {
        this.table = table;
    }

    fun select(vararg columns: String) {
        this.columns.addAll(columns)
    }

    fun where(expr: SqlExpression) {
        this.where = expr
    }
}

fun query(block: SqlSelectBuilder.() -> Unit) = SqlSelectBuilder().apply(block)

interface SqlExpression {
    fun build() : String
}

data class SqlCol(val name: String): SqlExpression {
    override fun build(): String  = name
}

data class SqlString(val value: String): SqlExpression {
    override fun build(): String  = "'$value'"
}

data class SqlNumber(val value: Number): SqlExpression {
    override fun build(): String  = value.toString()
}


data class SqlEq(val left: SqlExpression, val right: SqlExpression): SqlExpression {
    override fun build(): String = left.build() + " = " + right.build()
}

infix fun String.eq(arg: String) = SqlEq(SqlCol(this), SqlString(arg))

infix fun String.eq(arg: Number) = SqlEq(SqlCol(this), SqlNumber(arg))

data class SqlEqNull(val left: SqlExpression): SqlExpression {
    override fun build(): String = left.build() + " is null"
}

data class SqlNonEq(val left: SqlExpression, val right: SqlExpression): SqlExpression {
    override fun build(): String = left.build() + " != " + right.build()
}

data class SqlNonNull(val left: SqlExpression): SqlExpression {
    override fun build(): String = left.build() + " !is null"
}

infix fun String.nonEq(arg: String) = SqlNonEq(SqlCol(this), SqlString(arg))

infix fun String.nonEq(arg: Number) = SqlNonEq(SqlCol(this), SqlNumber(arg))

infix fun String.nonEq(arg: Nothing?) = SqlNonNull(SqlCol(this))

infix fun String.eq(arg: Nothing?) = SqlEqNull(SqlCol(this))

data class SqlOr(val expressions: List<SqlExpression>): SqlExpression {
    override fun build(): String = expressions.joinToString(" or ", prefix = "(", postfix = ")") { it.build() }
}

fun or(vararg expressions: SqlExpression) = SqlOr(expressions.asList())

infix fun SqlExpression.or(right: SqlExpression) = SqlOr(listOf(this, right))

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

    @Test
    fun `select certain columns from table 1`() {
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
}
