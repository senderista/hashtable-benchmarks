package set.int32;

import hash.int32.IntHasher;

import java.util.Arrays;


/**
 * An implementation of a simple linear probing hash table, with
 * the <a href="https://doi.org/10.1109/SFCS.1985.48">"Robin
 * Hood" insertion heuristic</a>, and a <a
 * href="http://codecapsule.com/2013/11/17/robin-hood-hashing-backward-shift-deletion/">tombstone-free
 * deletion algorithm</a> (note that because we do not use tombstones, we
 * cannot use the maximum insertion probe length to bound the probe length
 * of unsuccessful lookups; instead we use a Robin Hood-specific early
 * termination heuristic). The "Robin Hood" insertion heuristic dramatically
 * reduces the variance of probe length for successful lookups, and our
 * version also dramatically reduces both the expected value and variance
 * of unsuccessful lookups. The keys (there are no stored values) must be
 * 32-bit integers, which are permuted to form the hash codes (i.e., the
 * "hash function" is reversible). This obviates the need to separately store
 * hash codes or rehash the keys to perform operations which use hash codes.
 *
 * @author tdbaker
 */
public class RHIntHashSet extends LPIntHashSet {

    public RHIntHashSet(int maxEntries, double loadFactor, IntHasher hasher) {
        super(maxEntries, loadFactor, hasher);
    }

    public RHIntHashSet(int maxEntries, double loadFactor) {
        super(maxEntries, loadFactor);
    }

    /**
     * Add an element to the table.
     *
     * @param element the 32-bit integer to add to the table
     * @return {@code false} if {@code element} was already present in the table, {@code true} otherwise
     */
    @Override
    public boolean add(int element) {
        int hash = hash(element);
        if (lookupByHash(hash) != -1) {
            return false;
        }
        // If the current element is closer to its preferred bucket than
        // the element we're trying to insert is to its preferred bucket,
        // swap the inserted element into the current element's bucket and
        // continue probing with the swapped element, swapping it with the
        // next such element, and so on, until we hit an empty bucket.
        int bucket = findPreferredBucket(hash);
        int insertElemProbeDist = 0;
        int totalProbeLen = 0;
        while (!isEmpty(bucket)) {
            int currElemProbeDist = probeDistance(this.arr[bucket], bucket);
            if (currElemProbeDist < insertElemProbeDist) {
                int currElemHash = this.arr[bucket];
                this.arr[bucket] = hash;
                hash = currElemHash;
                insertElemProbeDist = currElemProbeDist;
            }
            bucket = wrap(bucket + 1);
            ++insertElemProbeDist;
            ++totalProbeLen;
            if (totalProbeLen == this.arr.length) {
                 throw new RuntimeException("Couldn't insert into table: " + element);
            }
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
    @Override
    public boolean remove(int value) {
        int hash = hash(value);
        int bucket = lookupByHash(hash);
        if (bucket == -1) {
            return false;
        }
        // find the chain from the deleted bucket to the nearest empty bucket
        // or entry in its preferred bucket and shift it one space to the left
        int endBucket = findMoveBoundary(wrap(bucket + 1));
        int currBucket = bucket;
        while (endBucket != wrap(currBucket + 1)) {
            this.arr[currBucket] = this.arr[wrap(currBucket + 1)];
            currBucket = wrap(currBucket + 1);
        }
        this.arr[currBucket] = 0;
        --this.size;
        return true;
    }

    @Override
    protected int lookupByHash(int hash) {
        int bucket = findPreferredBucket(hash);
        int probeLength = 0;
        while (!isEmpty(bucket)) {
            if (this.arr[bucket] == hash) {
                return bucket;
            }
            // If we're further from our element's preferred bucket than the
            // current element's distance from its preferred bucket, we know
            // the element is absent, since it would have been swapped with
            // the current element otherwise.
            if (probeLength == this.arr.length ||
                probeLength > probeDistance(this.arr[bucket], bucket)) {
                break;
            }
            bucket = wrap(bucket + 1);
            ++probeLength;
        }
        return -1;
    }

    private int probeDistance(int hash, int bucket) {
        int preferredBucket = findPreferredBucket(hash);
        int distance;
        if (preferredBucket > bucket) {  // wraparound
            distance = this.arr.length - preferredBucket + bucket;
        } else {
            distance = bucket - preferredBucket;
        }
        return distance;
    }

    // Any bucket to the right of its preferred bucket can be shifted left,
    // since its preferred bucket will be shifted along with it, ensuring
    // it is still reachable from its preferred bucket.
    private int findMoveBoundary(int startBucket) {
        int bucket = startBucket;
        assert startBucket < this.arr.length;
        while (!isEmpty(bucket) &&
               bucket != findPreferredBucket(this.arr[bucket])) {
            bucket = wrap(bucket + 1);
        }
        return bucket;
    }
}
