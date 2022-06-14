package ru.otus.visitor.practice

import mu.KotlinLogging
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.util.*
import javax.xml.parsers.SAXParserFactory

private val log = KotlinLogging.logger {}


data class Book(val id: Int, val name: String)


class MyHandler : DefaultHandler() {
    private lateinit var books_: MutableList<Book>
    private var elementValue: StringBuilder? = null

    val book: List<Book> get() = books_

    override fun characters(ch: CharArray, start: Int, length: Int) {
        log.info { "characters" }

        elementValue?.append(ch, start, length)
    }

    override fun startDocument() {
        log.info { "startDocument" }
        books_ = mutableListOf()
    }

    override fun startElement(uri: String, localName: String, name: String, attr: Attributes) {
        log.info { "startElement $uri $localName $name" }

        when (name) {
            "book" -> books_.add(Book(attr.getValue("id").toInt(), "unknown"))
        }

        elementValue = StringBuilder()
    }

    override fun endElement(uri: String, localName: String, name: String) {
        log.info { "endElement $uri $localName $name" }

        when (name) {
            "name" -> if (elementValue != null) {
                books_[books_.lastIndex] = books_.last().copy(name = elementValue.toString())
            }
        }
        elementValue = null
    }
}

fun main() {
    val factory = SAXParserFactory.newInstance()
    val saxParser = factory.newSAXParser()
    val handler = MyHandler()

    saxParser.parse("books.xml", handler);

    println(handler.book)
}