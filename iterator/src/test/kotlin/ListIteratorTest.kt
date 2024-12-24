import org.assertj.core.api.Assertions
import kotlin.test.Test

data class ListItem<T>(
    val value: T,
    val next: ListItem<T>? = null
): Iterable<T> {
    override fun iterator(): Iterator<T> = ListIterator(this)
}

class ListIterator<T>(private var current: ListItem<T>?): Iterator<T> {
    override fun hasNext(): Boolean = current != null

    override fun next(): T {
        val cur = current ?: throw NoSuchElementException()
        val result = cur.value
        current = cur.next
        return result
    }
}

class ListIteratorTest {

    @Test
    fun empty() {
        val list: ListItem<Int>? = null
        val iter = ListIterator(list)
        testEmpty(iter)
    }

    @Test
    fun notEmpty() {
        // 1 -> 2 -> 3
        val list = ListItem(1, ListItem(2, ListItem(3)))
        val iter = ListIterator(list)

        test123(iter)

        /*for(item in list) {
            println(item)
        }*/
        list.filter { it < 3 }.forEach { println(it) }

    }
}