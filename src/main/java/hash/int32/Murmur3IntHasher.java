package hash.int32;

/**
 * Implements Murmur3 32-bit finalizer (<a href="https://github.com/aappleby/smhasher/wiki/MurmurHash3">https://github.com/aappleby/smhasher/wiki/MurmurHash3</a>)
 *
 * @author tdbaker
 */
public class Murmur3IntHasher implements IntHasher {
    public static final String NAME = "hash.int32.Murmur3IntHasher";
    @Override
    public int hash(int x) {
        if (x == 0) {
            throw new IllegalArgumentException("Hashing 0 is a no-op");
        }
        x ^= x >>> 16;
        x *= 0x85ebca6b;
        x ^= x >>> 13;
        x *= 0xc2b2ae35;
        x ^= x >>> 16;
        return x;
    }

    @Override
    public int unhash(int x) {
        if (x == 0) {
            throw new IllegalArgumentException("Hashing 0 is a no-op");
        }
        x ^= x >>> 16;
        x *= 0x7ed1b41d;
        x ^= x >>> 13 ^ x >>> 26;
        x *= 0xa5cb9243;
        x ^= x >>> 16;
        return x;
    }

    @Override
    public IntHasher cloneHasher() {
        // stateless
        return new Murmur3IntHasher();
    }
}
