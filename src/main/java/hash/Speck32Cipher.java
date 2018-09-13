package hash;

/**
 * Speck32: 2 byte words, 7/2 rotation constants.
 * <p>
 * 20 base rounds (hypothetical)
 * <p>
 * 64 bit key/22 rounds.
 */
public final class Speck32Cipher
    extends SpeckIntCipher
{

    public Speck32Cipher()
    {
        this(20);
    }

    public Speck32Cipher(int rounds)
    {
        super(2, rounds, 7, 2);
    }

    @Override
    protected int mask(int val)
    {
        return (val & 0xffff);
    }

    @Override
    protected void checkKeySize(int keySizeBytes)
    {
        if (keySizeBytes != 8)
        {
            throw new IllegalArgumentException("Speck32 requires a key of 64 bits.");
        }
    }

    public int encrypt(int value)
    {
        // extract the left and right 16 bits of this int and put each of them in the low bits of an int
        int low = value & 0x0000ffff;
        int high = value & 0xffff0000;
        long encryptedBlob = this.encryptValue(low, high);
        // extract the low 16 bits from each 32-bit word in this long and concatenate them into an int
        high = (int) ((encryptedBlob & 0x0000ffff00000000L) >>> 32);
        low = (int) (encryptedBlob & 0x000000000000ffffL);
        return (high << 16) | low;
    }

    public int decrypt(int value)
    {
        // extract the left and right 16 bits of this int and put each of them in the low bits of an int
        int low = value & 0x0000ffff;
        int high = value & 0xffff0000;
        long decryptedBlob = this.decryptValue(low, high);
        // extract the low 16 bits from each 32-bit word in this long and concatenate them into an int
        high = (int) ((decryptedBlob & 0x0000ffff00000000L) >>> 32);
        low = (int) (decryptedBlob & 0x000000000000ffffL);
        return (high << 16) | low;
    }

}
