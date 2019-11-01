package hash.int32;

public class Prospector2RoundIntHasher implements IntHasher {
    public static final String NAME = "hash.int32.Prospector2RoundIntHasher";
    // https://github.com/skeeto/hash-prospector#two-round-functions
    @Override
    public int hash(int x) {
        if (x == 0) {
            throw new IllegalArgumentException("Hashing 0 is a no-op");
        }
        x ^= x >>> 16;
        x *= 0x7feb352d;
        x ^= x >>> 15;
        x *= 0x846ca68b;
        x ^= x >>> 16;
        return x;
    }

    @Override
    public int unhash(int x) {
        if (x == 0) {
            throw new IllegalArgumentException("Hashing 0 is a no-op");
        }
        x ^= x >>> 16;
        x *= 0x43021123;
        x ^= x >>> 15 ^ x >>> 30;
        x *= 0x1d69e2a5;
        x ^= x >>> 16;
        return x;
    }

    @Override
    public IntHasher cloneHasher() {
        // stateless
        return new Prospector2RoundIntHasher();
    }
}
