package set.int32;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.stream.IntStream;

class VerifyHashSet {

    private static final int SAMPLE_SIZE = 1 << 10;
    private static final long SEED = 0xdeadbeefcafebabeL;
    private static final double[] LOAD_FACTORS = {0.5, 0.75, 0.9, 0.99, 1.0};

    private static final Class<?>[] classes = {
        BLPIntHashSet.class,
        LCFSIntHashSet.class,
        LPIntHashSet.class,
        RHIntHashSet.class,
    };

    @Test
    void testRandomKeys() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        for (double loadFactor : LOAD_FACTORS) {
            for (Class<?> cls : classes) {
                Constructor<?> constructor = cls.getConstructor(int.class, double.class);
                IntSet set = (IntSet) constructor.newInstance(SAMPLE_SIZE, loadFactor);
                int[] ints = new Random(SEED).ints(SAMPLE_SIZE).toArray();
                for (int i : ints) {
                    // if this ever happens, then we'll just pick a different seed
                    assert i != 0;
                }
                for (int i : ints) {
                    assertTrue(set.add(i));
                }
                assertEquals(SAMPLE_SIZE, set.size());
                for (int i : ints) {
                    assertTrue(set.contains(i));
                }
                for (int i : ints) {
                    assertTrue(set.remove(i));
                }
                assertEquals(0, set.size());
                for (int i : ints) {
                    assertFalse(set.contains(i));
                }
            }
        }
    }

    @Test
    void testSequentialKeys() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        for (double loadFactor : LOAD_FACTORS) {
            for (Class<?> cls : classes) {
                Constructor<?> constructor = cls.getConstructor(int.class, double.class);
                IntSet set = (IntSet) constructor.newInstance(SAMPLE_SIZE, loadFactor);
                int[] ints = IntStream.rangeClosed(1, SAMPLE_SIZE).toArray();
                for (int i : ints) {
                    assertTrue(set.add(i));
                }
                assertEquals(SAMPLE_SIZE, set.size());
                for (int i : ints) {
                    assertTrue(set.contains(i));
                }
                for (int i : ints) {
                    assertTrue(set.remove(i));
                }
                assertEquals(0, set.size());
                for (int i : ints) {
                    assertFalse(set.contains(i));
                }
            }
        }
    }

}
