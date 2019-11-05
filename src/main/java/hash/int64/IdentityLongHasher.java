package hash.int64;

/**
 * Implements identity hash function.
 *
 * @author tdbaker
 */
public class IdentityLongHasher implements LongHasher {
    public static final String NAME = "hash.int64.IdentityLongHasher";

    @Override
    public long hash(long x) {
        return x;
    }

    @Override
    public long unhash(long x) {
        return x;
    }

    @Override
    public LongHasher cloneHasher() {
        // stateless
        return new IdentityLongHasher();
    }
}
