package hash.int64;

public class DegskiLongHasher implements LongHasher {
    public static final String NAME = "hash.int64.DegskiLongHasher";
    // https://gist.github.com/degski/6e2069d6035ae04d5d6f64981c995ec2
    @Override
    public long hash(long x) {
        if (x == 0) {
            throw new IllegalArgumentException("Hashing 0 is a no-op");
        }
        x ^= x >>> 32;
        x *= 0xD6E8FEB86659FD93L;
        x ^= x >>> 32;
        x *= 0xD6E8FEB86659FD93L;
        x ^= x >>> 32;
        return x;
    }

    @Override
    public long unhash(long x) {
        if (x == 0) {
            throw new IllegalArgumentException("Hashing 0 is a no-op");
        }
        x ^= x >>> 32;
        x *= 0xCFEE444D8B59A89BL;
        x ^= x >>> 32;
        x *= 0xCFEE444D8B59A89BL;
        x ^= x >>> 32;
        return x;
    }
}