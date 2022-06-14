package ru.otus.decorator.practice

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun doSomething(stream: OutputStream) = stream.use {
    stream.write("hello\n".toByteArray())
    stream.write("world".toByteArray())
}

fun main() {
    var file : OutputStream = FileOutputStream("file.zip")
    file = BufferedOutputStream(file)
    //val zip = ZipOutputStream(file)
    //zip.putNextEntry(ZipEntry("file"))
    val obj = ObjectOutputStream(file)



    doSomething(file)

  ///  zip.close()
}