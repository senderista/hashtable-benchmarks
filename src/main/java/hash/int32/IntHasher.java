package hash.int32;

/**
 * A 32-bit hash function.
 *
 * @author tdbaker
 */
public interface IntHasher {
    /**
     * Apply the hash function to a value.
     * @param x the 32-bit integer to hash
     * @return the hashed value
     */
    public int hash(int x);
    /**
     * Invert the hash function.
     * @param x the 32-bit integer to unhash
     * @return the unhashed value
     */
    public int unhash(int x);
    /**
     * Clone the hash function object.
     *
     * @return the cloned hasher
     */
    public IntHasher cloneHasher();
}
