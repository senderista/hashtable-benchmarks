package hash.int64;

/**
 * Implements Murmur3 64-bit finalizer (<a href="https://github.com/aappleby/smhasher/wiki/MurmurHash3">https://github.com/aappleby/smhasher/wiki/MurmurHash3</a>)
 *
 * @author tdbaker
 */
public class Murmur3LongHasher implements LongHasher {
    public static final String NAME = "hash.int64.Murmur3LongHasher";
    @Override
    public long hash(long x) {
        if (x == 0) {
            throw new IllegalArgumentException("Hashing 0 is a no-op");
        }
        x ^= x >>> 33;
        x *= 0xff51afd7ed558ccdL;
        x ^= x >>> 33;
        x *= 0xc4ceb9fe1a85ec53L;
        x ^= x >>> 33;
        return x;
    }

    @Override
    public long unhash(long x) {
        if (x == 0) {
            throw new IllegalArgumentException("Hashing 0 is a no-op");
        }
        x ^= x >>> 33;
        x *= 0x9cb4b2f8129337dbL;
        x ^= x >>> 33;
        x *= 0x4f74430c22a54005L;
        x ^= x >>> 33;
        return x;
    }

    @Override
    public LongHasher cloneHasher() {
        // stateless
        return new Murmur3LongHasher();
    }
}
