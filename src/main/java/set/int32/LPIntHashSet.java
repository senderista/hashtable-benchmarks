package set.int32;

import hash.int32.IntHasher;
import hash.int32.Murmur3IntHasher;

import java.util.Arrays;


/**
 * An implementation of a simple linear probing hash table, with a
 * tombstone-free deletion algorithm taken from _Algorithm Design and
 * Applications_, Section 6.3.3. (Note that because we do not use tombstones,
 * we cannot use the maximum insertion probe length to bound the probe length
 * of unsuccessful lookups.) The keys (there are no stored values) must be
 * 32-bit integers, which are permuted to form the hash codes (i.e., the
 * "hash function" is reversible). This obviates the need to separately store
 * hash codes or rehash the keys to perform operations which use hash codes.
 *
 * @author tdbaker
 */
public class LPIntHashSet implements IntSet {
    public static final String NAME = "set.int32.LPIntHashSet";

    protected final int[] arr;
    protected int size = 0;
    protected final IntHasher hasher;

    public LPIntHashSet(int maxEntries, double loadFactor, IntHasher hasher) {
        assert maxEntries > 0;
        assert loadFactor > 0 && loadFactor <= 1.0;
        int arrSize = (int) (maxEntries / loadFactor);
        this.arr = new int[arrSize];
        this.hasher = hasher;
    }

    public LPIntHashSet(int maxEntries, double loadFactor) {
        this(maxEntries, loadFactor, new Murmur3IntHasher());
    }

    public LPIntHashSet(LPIntHashSet other) {
        this.arr = other.arr.clone();
        this.size = other.size;
        this.hasher = other.hasher.cloneHasher();
    }

    /**
     * Return deep copy of the table.
     */
    public IntSet cloneSet() throws CloneNotSupportedException {
        return new LPIntHashSet(this);
    }

    /**
     * Query the size of the table's backing array.
     *
     * @return the size of the backing array
     */
    public int capacity() {
        return this.arr.length;
    }

    /**
     * Query the number of elements in the table.
     *
     * @return the number of elements in the table
     */
    public int size() {
        assert this.size >= 0;
        return this.size;
    }

    /**
     * Query the table for a value.
     *
     * @param value the 32-bit integer to query the table for
     * @return {@code true} if {@code value} is present in the table, {@code false} otherwise
     */
    public boolean contains(int value) {
        int hash = hash(value);
        int bucket = lookupByHash(hash);
        if (bucket == -1 || isEmpty(bucket)) {
            return false;
        }
        return true;
    }

    /**
     * Add an element to the table.
     *
     * @param element the 32-bit integer to add to the table
     * @return {@code false} if {@code element} was already present in the table, {@code true} otherwise
     */
    public boolean add(int element) {
        int hash = hash(element);
        int bucket = lookupByHash(hash);
        if (bucket == -1) {
            // table full
            throw new RuntimeException("Couldn't insert into table");
        }
        if (!isEmpty(bucket)) {
            return false;
        }
        this.arr[bucket] = hash;
        ++this.size;
        return true;
    }

    /**
     * Remove an element from the table.
     *
     * @param value the 32-bit integer to remove from the table
     * @return {@code false} if {@code value} was not present in the table, {@code true} otherwise
     */
    public boolean remove(int value) {
        int hash = hash(value);
        int bucket = lookupByHash(hash);
        if (bucket == -1 || isEmpty(bucket)) {
            return false;
        }
        this.arr[bucket] = 0;
        shift(bucket);
        --this.size;
        return true;
    }

    /**
     * Remove all elements from the table.
     */
    public void clear() {
        Arrays.fill(this.arr, 0);
    }

    protected boolean isEmpty(int bucket) {
        return (this.arr[bucket] == 0);
    }

    protected int contents(int bucket) {
        return isEmpty(bucket) ? 0 : unhash(this.arr[bucket]);
    }

    // https://github.com/lemire/fastrange
    protected int findPreferredBucket(int hash) {
        if (hash == 0) {
            return -1;
        }
        return (int) ((Integer.toUnsignedLong(hash) * Integer.toUnsignedLong(this.arr.length)) >>> 32);
    }

    protected int wrap(int pos) {
        if (pos < 0) {
            return this.arr.length + pos;
        }
        if (pos > this.arr.length - 1) {
            return pos - this.arr.length;
        }
        return pos;
    }

    protected int hash(int x) {
        return this.hasher.hash(x);
    }

    protected int unhash(int x) {
        return this.hasher.unhash(x);
    }

    protected int lookupByHash(int hash) {
        int bucket = findPreferredBucket(hash);
        int probeLength = 0;
        while (!isEmpty(bucket) && this.arr[bucket] != hash) {
            if (probeLength == this.arr.length) {
                return -1;
            }
            bucket = wrap(bucket + 1);
            ++probeLength;
        }
        return bucket;
    }

    // uses pseudocode from _Algorithm Design and Applications_, Section 6.3.3
    protected void shift(int startBucket) {
        int dst = startBucket;
        int shift = 1;
        int src = wrap(dst + shift);
        while (!isEmpty(src)) {
             int preferredBucket = findPreferredBucket(this.arr[src]);
             // we can only move a key if its destination can be reached from its preferred bucket
             boolean reachable;
             if (src <= dst) {
                reachable = (preferredBucket <= dst && preferredBucket > src);
             } else {
                reachable = (preferredBucket <= dst || preferredBucket > src);
             }
             if (reachable) {
                this.arr[dst] = this.arr[src];  // fill the hole
                this.arr[src] = 0;  // move the hole
                dst = wrap(dst + shift);
                shift = 1;
             } else {
                ++shift;
             }
             src = wrap(dst + shift);
        }
    }

    protected void dump() {
        for (int i = 0; i < this.arr.length; ++i) {
            System.out.format("%d\t%d\t%s\t%d\n", i, contents(i), Integer.toUnsignedString(this.arr[i]), findPreferredBucket(this.arr[i]));
        }
    }
}
