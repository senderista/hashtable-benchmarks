package hash.int32;

public interface IntHasher {
    public int hash(int x);
    public int unhash(int x);
    public IntHasher cloneHasher();
}
