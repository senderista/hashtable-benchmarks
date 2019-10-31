package hash.int32;

public class PhiIntHasher implements IntHasher {
    public static final String NAME = "hash.int32.PhiIntHasher";
    // https://raw.githubusercontent.com/vigna/fastutil/master/src/it/unimi/dsi/fastutil/HashCommon.java
    private static final int INT_PHI = 0x9e3779b9;
    private static final int INV_INT_PHI = 0x144cbc89;

    @Override
    public int hash(int x) {
        if (x == 0) {
            throw new IllegalArgumentException("Hashing 0 is a no-op");
        }
        x *= INT_PHI;
        x ^= x >>> 16;
        return x;
    }

    @Override
    public int unhash(int x) {
        if (x == 0) {
            throw new IllegalArgumentException("Hashing 0 is a no-op");
        }
        x ^= x >>> 16;
        x *= INV_INT_PHI;
        return x;
    }
}
