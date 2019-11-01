package hash.int64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import java.util.Random;

import java.lang.reflect.InvocationTargetException;

class VerifyHashInverses {

    private static final long SAMPLE_SIZE = 1L << 10;
    private static final long SEED = 0xdeadbeefcafebabeL;

    private static final Class<?>[] classes = {
        DegskiLongHasher.class,
        IdentityLongHasher.class,
        Murmur3LongHasher.class,
        PhiLongHasher.class,
        // SpeckLongHasher.class,
        Variant13LongHasher.class,
        WangLongHasher.class,
    };

    @Test
    void test()
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        for (Class<?> cls : classes) {
            LongHasher hasher = (LongHasher) cls.getDeclaredConstructor().newInstance();
            new Random(SEED).longs(SAMPLE_SIZE).forEach(i -> assertEquals(i, hasher.unhash(hasher.hash(i)),
                    String.format("%s: inverting hash for value %d", cls.getName(), i)));
            // all current hash functions map 0 to itself
            if (!cls.equals(IdentityLongHasher.class)) {
                assertThrows(IllegalArgumentException.class, () -> hasher.hash(0L), cls.getName());
            }
        }
    }

}
