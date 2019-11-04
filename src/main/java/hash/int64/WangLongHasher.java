package hash.int64;

public class WangLongHasher implements LongHasher {
    public static final String NAME = "hash.int64.WangLongHasher";
    // https://naml.us/post/inverse-of-a-hash-function/
    @Override
    public long hash(long x) {
        if (x == 0) {
            throw new IllegalArgumentException("Hashing 0 is a no-op");
        }
        x = (~x) + (x << 21); // x = (x << 21) - x - 1;
        x = x ^ (x >>> 24);
        x = (x + (x << 3)) + (x << 8); // x * 265
        x = x ^ (x >>> 14);
        x = (x + (x << 2)) + (x << 4); // x * 21
        x = x ^ (x >>> 28);
        x = x + (x << 31);
        return x;
    }

    @Override
    public long unhash(long x) {
        if (x == 0) {
            throw new IllegalArgumentException("Hashing 0 is a no-op");
        }

        long tmp;

        // Invert x = x + (x << 31)
        tmp = x - (x << 31);
        x = x - (tmp << 31);

        // Invert x = x ^ (x >> 28)
        tmp = x ^ x >>> 28;
        x = x ^ tmp >>> 28;

        // Invert x *= 21
        x *= -3513665537849438403L;

        // Invert x = x ^ (x >> 14)
        tmp = x ^ x >>> 14;
        tmp = x ^ tmp >>> 14;
        tmp = x ^ tmp >>> 14;
        x = x ^ tmp >>> 14;

        // Invert x *= 265
        x *= -3202076329775997639L;

        // Invert x = x ^ (x >> 24)
        tmp = x ^ x >>> 24;
        x = x ^ tmp >>> 24;

        // Invert x = (~x) + (x << 21)
        tmp = ~x;
        tmp = ~(x - (tmp << 21));
        tmp = ~(x - (tmp << 21));
        x = ~(x - (tmp << 21));

        return x;
    }

    @Override
    public LongHasher cloneHasher() {
        // stateless
        return new WangLongHasher();
    }
}
