package hash.int32;

public class IdentityIntHasher implements IntHasher {
    @Override
    public int hash(int x) {
        return x;
    }

    @Override
    public int unhash(int x) {
        return x;
    }
}
