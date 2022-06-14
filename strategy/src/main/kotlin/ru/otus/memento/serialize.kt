package ru.otus.memento

import kotlinx.serialization.json.Json
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

data class Person(val name : String, val year: Int, @Transient val accNumber: String) : java.io.Serializable {

}

fun main() {
    // Serializing objects
    val data = Person("Ivan", 42, "x")
    val outputStream = ByteArrayOutputStream()
    ObjectOutputStream(outputStream).writeObject(data)
    val store = outputStream.toByteArray()

    println(store.contentToString())

    // Deserializing back into objects
    val obj = ObjectInputStream(ByteArrayInputStream(store)).readObject()
    println(obj)
}