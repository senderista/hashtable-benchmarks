package hash.int64;

public class Variant13LongHasher implements LongHasher {
    // Variant 13 of Murmur3 64-bit finalizer (http://zimbry.blogspot.com/2011/09/better-bit-mixing-improving-on.html)
    @Override
    public long hash(long x) {
        if (x == 0) {
            throw new IllegalArgumentException("Hashing 0 is a no-op");
        }
        x ^= x >>> 30;
        x *= 0xbf58476d1ce4e5b9L;
        x ^= x >>> 27;
        x *= 0x94d049bb133111ebL;
        x ^= x >>> 31;
        return x;
    }

    @Override
    public long unhash(long x) {
        if (x == 0) {
            throw new IllegalArgumentException("Hashing 0 is a no-op");
        }
        x ^= x >>> 31 ^ x >>> 62;
        x *= 0x319642b2d24d8ec3L;
        x ^= x >>> 27 ^ x >>> 54;
        x *= 0x96de1b173f119089L;
        x ^= x >>> 30 ^ x >>> 60;
        return x;
    }
}
