package ru.otus

import java.util.stream.Collectors

fun main() {
    /*for (e in 10 downTo 1) {
        println(e)
    }*/

    val col = listOf(1, 2, 3)

    col
       // .asSequence()
        .filter {
            println("filter: $it")
            it < 3
        }
        .map {
            println("map: $it")
            it * 2
        }
      //  .first { it < 3}

//    val iter: Iterator<Int> = MyListIterator(top)
//
//    while (iter.hasNext()) {
//        val elem = iter.next()
//        println(">" + elem)
//        println(">>>" + elem)
//    }


    /*val x = listOf(1, 2, 3, 4, 1).asSequence()
        .map {
            println(it)
            it
        }
        .map { it * 2 }
        .first { it < 5 }
        //.collect(Collectors.toList())
    println("==" + x)*/
    /*var i = 0
    while (i < 100) {
        println(i)
        //if (i < 50) continue

        println(i+1)
        ++i
    }*/

    /*for (i in 1 .. 100 step 10) {
        println(i)
    }*/

    //repeat(100) { println(it) }
}