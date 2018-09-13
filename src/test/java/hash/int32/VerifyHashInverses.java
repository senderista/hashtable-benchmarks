package hash.int32;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.IntStream;

class VerifyHashInverses {

    private static final int SAMPLE_SIZE = 1 << 10;
    private static final long SEED = 0xdeadbeefcafebabeL;

    private static final Class<?>[] classes = {
        H2IntHasher.class,
        IdentityIntHasher.class,
        Murmur3IntHasher.class,
        PhiIntHasher.class,
        Prospector2RoundIntHasher.class,
        Prospector3RoundIntHasher.class,
        // SpeckIntHasher.class,
    };

    @Test
    void test() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        for (Class<?> cls : classes) {
            IntHasher hasher = (IntHasher) cls.newInstance();
            new Random(SEED).ints(SAMPLE_SIZE).forEach(
                i -> assertEquals(i, hasher.unhash(hasher.hash(i)),
                    String.format("%s: inverting hash for value %d", cls.getName(), i))
            );
            // all current hash functions map 0 to itself
            if (!cls.equals(IdentityIntHasher.class)) {
                assertThrows(IllegalArgumentException.class, () -> hasher.hash(0), cls.getName());
            }
        }
    }

}
