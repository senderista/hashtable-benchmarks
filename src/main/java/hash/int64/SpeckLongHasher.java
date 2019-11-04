package hash.int64;

public class SpeckLongHasher implements LongHasher {
    public static final String NAME = "hash.int64.SpeckLongHasher";
    // 64-bit Speck block cipher requires 128-bit key, default 25 rounds
    private final Speck64Cipher encryptor;
    private final Speck64Cipher decryptor;
    private final byte[] key;
    private final int rounds;

    public SpeckLongHasher(byte[] key, int rounds) {
        this.encryptor = new Speck64Cipher(rounds);
        this.decryptor = new Speck64Cipher(rounds);
        encryptor.init(true, key);
        decryptor.init(false, key);
    }

    @Override
    public long hash(long x) {
        return encryptor.processBlock(x);
    }
    @Override
    public long unhash(long x){
        return decryptor.processBlock(x);
    }

    @Override
    public LongHasher cloneHasher() {
        // NYI
    }
}
