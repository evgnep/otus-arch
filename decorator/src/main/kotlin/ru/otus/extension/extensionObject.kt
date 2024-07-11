package ru.otus.extension

interface PartExtension

interface ToCsvExtension : PartExtension {
    fun toCsv(obj: Any): String
}

open class ExtensionsHolder {
    private val map = mutableMapOf<String, PartExtension>()

    fun add(name: String, ext: PartExtension) {
        map[name] = ext
    }

    fun get(name: String): PartExtension? = map[name]
}

class Rectangle(val width: Int, val height: Int) : ExtensionsHolder()

class Circle(val radius: Int) : ExtensionsHolder()

class RectangleToCsv : ToCsvExtension {
    override fun toCsv(obj: Any): String {
        if (obj !is Rectangle) throw RuntimeException()
        return "" + obj.height + "," + obj.width
    }
}

class CircleToCsv : ToCsvExtension {
    override fun toCsv(obj: Any): String {
        if (obj !is Circle) throw RuntimeException()
        return "" + obj.radius + ","
    }
}

fun rectangleFactory(width: Int, height: Int) = Rectangle(width, height).also {
    it.add("csv", RectangleToCsv())
}

fun main() {
    val rectangle = rectangleFactory(10, 20)
    val csv = rectangle.get("csv").let { (it as ToCsvExtension).toCsv(rectangle)  }
    println(csv)
}