package hash.int64;

/**
 * A 64-bit hash function.
 *
 * @author tdbaker
 */
public interface LongHasher {
    /**
     * Apply the hash function to a value.
     * @param x the 64-bit integer to hash
     * @return the hashed value
     */
    public long hash(long x);
    /**
     * Invert the hash function.
     * @param x the 64-bit integer to hash
     * @return the unhashed value
     */
    public long unhash(long x);
    /**
     * Clone the hash function object.
     *
     * @return the cloned hasher
     */
    public LongHasher cloneHasher();
}
