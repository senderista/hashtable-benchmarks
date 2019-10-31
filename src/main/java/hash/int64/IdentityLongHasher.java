package hash.int64;

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
}
