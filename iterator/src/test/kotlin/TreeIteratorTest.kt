import org.junit.jupiter.api.Test
import java.lang.Thread.yield

class TreeItem<T>(
    val value: T,
    val left: TreeItem<T>? = null,
    val right: TreeItem<T>? = null
) : Iterable<T> {
    override fun iterator(): Iterator<T> = TreeIterator(this)
}

class TreeIterator<T>(private val root: TreeItem<T>?) : Iterator<T> {
    private val nodesToVisit = mutableListOf<TreeItem<T>>()

    init {
        if (root != null) nodesToVisit.add(root)
    }

    override fun hasNext(): Boolean = nodesToVisit.isNotEmpty()

    override fun next(): T {
        if (!hasNext()) throw NoSuchElementException()
        val cur = nodesToVisit.removeLast()
        cur.left?.run { nodesToVisit.add(this) }
        cur.right?.run { nodesToVisit.add(this) }
        return cur.value
    }
}



class TreeIteratorTest {
    @Test
    fun generator() {
        val a = TreeItem(1)
        val b = TreeItem(3)
        val c = TreeItem(2, a, b)
        val root = TreeItem(0, c)

        val seq = sequence {
            val list = mutableListOf<TreeItem<Int>>()
            list.add(root)
            while (list.isNotEmpty()) {
                val cur = list.removeLast()
                println("yield " + cur.value)
                yield(cur.value)
                println("after yield " + cur.value)
                if (cur.left != null) list.add(cur.left)
                if (cur.right != null) list.add(cur.right)
            }
        }
        println(seq.first { it > 0 })
    }

    @Test
    fun iterator() {
        val a = TreeItem(1)
        val b = TreeItem(3)
        val c = TreeItem(2, a, b)
        val root = TreeItem(0, c)

        for (e in root) {
            println(e)
        }
    }
}