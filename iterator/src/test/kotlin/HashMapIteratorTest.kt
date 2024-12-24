import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import kotlin.math.abs


private const val BUCKET_COUNT = 10

class MyHashMap<K, V>: Iterable<Pair<K, V>> {

    private val buckets: MutableList<
            MutableList<Pair<K, V>>
            > = mutableListOf()

    init {
        repeat(BUCKET_COUNT) { buckets.add(mutableListOf()) }
    }

    fun put(key: K, value: V) {
        val bucketNo = abs(key.hashCode()) % BUCKET_COUNT
        val pair = key to value
        buckets[bucketNo].add(pair)
    }

    fun get(key: K): V? {
        val bucketNo = abs(key.hashCode()) % BUCKET_COUNT
        val bucket = buckets[bucketNo]
        return bucket.firstOrNull { it.first == key }?.second
    }

    override fun iterator(): Iterator<Pair<K, V>> {
        return MyHashMapIterator(this)
    }

    class MyHashMapIterator<K, V>(private val source: MyHashMap<K, V>): Iterator<Pair<K, V>> {
        private var bucketNo = 0
        private var elementInBucketNo = -1

        override fun hasNext(): Boolean {
            while (true) {
                if (bucketNo >= BUCKET_COUNT) return false
                val bucket = source.buckets[bucketNo]
                if (elementInBucketNo + 1 < bucket.size) {
                    return true
                }
                bucketNo += 1
                elementInBucketNo = -1
            }
        }

        override fun next(): Pair<K, V> {
            while (true) {
                if (bucketNo >= BUCKET_COUNT) throw NoSuchElementException()
                val bucket = source.buckets[bucketNo]
                if (elementInBucketNo + 1 < bucket.size) {
                    return bucket[++elementInBucketNo]
                }
                bucketNo += 1
                elementInBucketNo = -1
            }
        }
    }
}

class HashMapIteratorTest {

    @Test
    fun empty() {
        val map = MyHashMap<String, Int>()
        val iter = map.iterator()

        assertThat(iter.hasNext()).isFalse()
        assertThatThrownBy { iter.next() }
    }

    @Test
    fun nonEmpty() {
        val map = MyHashMap<String, Int>()
        map.put("Qqqq", 1)
        map.put("Bbb", 2)
        map.put("Cccc", 3)

        for(elem in map) {
            println(elem)
        }
    }
}