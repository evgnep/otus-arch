import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class ArrayIterator<T>(private val source: Array<T>)
    : Iterator<T> {

    private var pos = -1

    override fun hasNext(): Boolean = pos + 1 < source.size

    override fun next(): T {
        if (!hasNext()) throw NoSuchElementException()
        return source[++pos]
    }
}

fun testEmpty(iter: Iterator<Int>) {
    assertThat(iter.hasNext()).isFalse()
    assertThatThrownBy { iter.next() }
}

fun test123(iter: Iterator<Int>) {
    assertThat(iter.hasNext()).isTrue()
    assertThat(iter.next()).isEqualTo(1)

    assertThat(iter.hasNext()).isTrue()
    assertThat(iter.next()).isEqualTo(2)

    assertThat(iter.hasNext()).isTrue()
    assertThat(iter.next()).isEqualTo(3)

    assertThat(iter.hasNext()).isFalse()
    assertThatThrownBy { iter.next() }
}

class ArrayIteratorTest {
    @Test
    fun empty() {
        val array = arrayOf<Int>()
        val iter = ArrayIterator(array)
        testEmpty(iter)
    }

    @Test
    fun nonEmpty() {
        val array = arrayOf(1, 2, 3)
        val iter = ArrayIterator(array)
        test123(iter)

       // val iter2 = ArrayIterator(array)
        for(item in array) {
            println(item)
        }
    }

}