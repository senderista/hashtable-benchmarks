package hash.int64;

public class PhiLongHasher implements LongHasher {
    public static final String NAME = "hash.int64.PhiLongHasher";
    // https://raw.githubusercontent.com/vigna/fastutil/master/src/it/unimi/dsi/fastutil/HashCommon.java
    private static final long LONG_PHI = 0x9e3779b97f4a7c15l;
    private static final long INV_LONG_PHI = 0xf1de83e19937733dL;

    @Override
    public long hash(long x) {
        if (x == 0) {
            throw new IllegalArgumentException("Hashing 0 is a no-op");
        }
        x *= LONG_PHI;
        x ^= x >>> 32;
        x ^= x >>> 16;
        return x;
    }

    @Override
    public long unhash(long x) {
        if (x == 0) {
            throw new IllegalArgumentException("Hashing 0 is a no-op");
        }
        x ^= x >>> 32;
        x ^= x >>> 16;
        x ^= x >>> 32;
        x *= INV_LONG_PHI;
        return x;
    }
}
