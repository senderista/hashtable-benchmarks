package set.int64;

import hash.int64.LongHasher;


/**
 * An implementation of a simple linear probing hash table,
 * with a tombstone-free deletion algorithm taken from
 * _Algorithm Design and Applications_, Section 6.3.3, and the <a
 * href="https://doi.org/10.1016/0196-6774(89)90014-X">Last-Come-First-Served
 * insertion heuristic</a>. The LCFS heuristic dramatically reduces the
 * variance of probe length for successful lookups, though it has no effect
 * on its expected value, nor does it have any effect at all on unsuccessful
 * lookups (note that because we do not use tombstones, we cannot use the
 * maximum insertion probe length to bound the probe length of unsuccessful
 * lookups). The keys (there are no stored values) must be 64-bit integers,
 * which are permuted to form the hash codes (i.e., the "hash function"
 * is reversible). This obviates the need to separately store hash codes
 * or rehash the keys to perform operations which use hash codes.
 *
 * @author tdbaker
 */
public class LCFSLongHashSet extends LPLongHashSet {
    public static final String NAME = "set.int64.LCFSLongHashSet";

    public LCFSLongHashSet(int maxEntries, double loadFactor, LongHasher hasher) {
        super(maxEntries, loadFactor, hasher);
    }

    public LCFSLongHashSet(int maxEntries, double loadFactor) {
        super(maxEntries, loadFactor);
    }

    public LCFSLongHashSet(LCFSLongHashSet other) throws CloneNotSupportedException {
        super(other);
    }

    /**
     * Return deep copy of the table.
     */
    @Override
    public LongSet cloneSet() throws CloneNotSupportedException {
        return new LCFSLongHashSet(this);
    }

    /**
     * Add an element to the table.
     *
     * @param element the 64-bit integer to add to the table
     * @return {@code false} if {@code element} was already present in the table, {@code true} otherwise
     */
    @Override
    public boolean add(long element) {
        long hash = hash(element);
        int bucket = lookupByHash(hash);
        if (bucket == -1) {
            // table full
            throw new RuntimeException("Couldn't insert into table");
        }
        if (!isEmpty(bucket)) {
            return false;
        }
        int preferredBucket = findPreferredBucket(hash);
        int emptyBucket = findFirstEmptyBucket(preferredBucket);
        // This is the "Last-Come-First-Served" heuristic: we always insert the
        // element in its preferred bucket and shift the chain starting at that
        // bucket one space to the right.
        moveEmptyBucketToInsertionPoint(emptyBucket, preferredBucket);
        this.arr[preferredBucket] = hash;
        ++this.size;
        return true;
    }

    private int findFirstEmptyBucket(int startBucket) {
        assert startBucket >= 0 && startBucket < this.arr.length;
        int bucket = startBucket;
        int probeLength = 0;
        while (!isEmpty(bucket)) {
            if (probeLength == this.arr.length) {
                return -1;
            }
            bucket = wrap(bucket + 1);
            ++probeLength;
        }
        return bucket;
    }

    private void moveEmptyBucketToInsertionPoint(int startBucket, int endBucket) {
        assert startBucket >= 0 && startBucket < this.arr.length;
        assert isEmpty(startBucket);
        int bucket = startBucket;
        while (bucket != endBucket) {
            this.arr[bucket] = this.arr[wrap(bucket - 1)];
            bucket = wrap(bucket - 1);
        }
    }
}
