package hash.int64;

public class IdentityLongHasher implements LongHasher {
    @Override
    public long hash(long x) {
        return x;
    }

    @Override
    public long unhash(long x) {
        return x;
    }
}
