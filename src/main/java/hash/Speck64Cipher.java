package hash;

/**
 * Speck64: 4 byte words, 8/3 rotation constants.
 * <p>
 * 25 base rounds (hypothetical)
 * <p>
 * 96 bit key/26 rounds.<br>
 * 128 bit key/27 rounds.
 */
public final class Speck64Cipher
    extends SpeckIntCipher
{

    public Speck32Cipher()
    {
        this(25);
    }

    public Speck32Cipher(int rounds)
    {
        super(4, rounds);
    }

    @Override
    protected int mask(int val)
    {
        return val;
    }

    @Override
    protected void checkKeySize(int keySizeBytes)
    {
        if (keySizeBytes != 12 && keySizeBytes != 16)
        {
            throw new IllegalArgumentException("Speck64 requires a key of 96 or 128 bits.");
        }
    }

    public int encrypt(long value)
    {
        // extract the left and right 32 bits of this int
        int low = value & 0x00000000ffffffffL;
        int high = value & 0xffffffff00000000L;
        return this.encryptValue(low, high);
    }

    public int decrypt(int value)
    {
        // extract the left and right 32 bits of this int
        int low = value & 0x00000000ffffffffL;
        int high = value & 0xffffffff00000000L;
        return this.decryptValue(low, high);
    }
}
