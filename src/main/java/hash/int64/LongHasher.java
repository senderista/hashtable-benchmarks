package hash.int64;

public interface LongHasher {
    public long hash(long x);
    public long unhash(long x);
}
