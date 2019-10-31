package hash.int32;

public class SpeckIntHasher implements IntHasher {
    public static final String NAME = "hash.int32.SpeckIntHasher";
    // 32-bit Speck block cipher requires 64-bit key, default 20 rounds
    private static final long defaultKey = 12235564383205437408L;
    private final Speck32Cipher encryptor;
    private final Speck32Cipher decryptor;
    private final byte[] key;
    private final int rounds;

    public SpeckIntHasher(long key, int rounds) {
        this.encryptor = new Speck32Cipher(rounds);
        this.decryptor = new Speck32Cipher(rounds);
        encryptor.init(true, key);
        decryptor.init(false, key);
    }

    public SpeckIntHasher() {
        this(defaultKey, 20);
    }

    @Override
    public int hash(int x) {
        return encryptor.processBlock(x);
    }
    @Override
    public int unhash(int x){
        return decryptor.processBlock(x);
    }
}
