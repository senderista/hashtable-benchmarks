package hash.int32;

/**
 * Implements identity hash function.
 *
 * @author tdbaker
 */
public class IdentityIntHasher implements IntHasher {
    public static final String NAME = "hash.int32.IdentityIntHasher";

    @Override
    public int hash(int x) {
        return x;
    }

    @Override
    public int unhash(int x) {
        return x;
    }

    @Override
    public IntHasher cloneHasher() {
        // stateless
        return new IdentityIntHasher();
    }
}
