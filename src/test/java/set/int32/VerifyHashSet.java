package set.int32;

import hash.int32.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.IntStream;

@RunWith(JUnitQuickcheck.class)
public class VerifyHashSet {

    private static final int SAMPLE_SIZE_LIMIT = 1 << 20;

    private static final Class<?>[] classes = { BLPIntHashSet.class, LCFSIntHashSet.class, LPIntHashSet.class,
            RHIntHashSet.class, };

    @Property
    public void testSequentialKeys(@InRange(minInt = 0, maxInt = SAMPLE_SIZE_LIMIT) int sampleSize,
            @InRange(minDouble = 0.0, maxDouble = 1.0) double loadFactor)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        for (Class<?> cls : classes) {
            Constructor<?> constructor = cls.getConstructor(int.class, double.class);
            IntSet set = (IntSet) constructor.newInstance(sampleSize, loadFactor);
            int[] ints = IntStream.rangeClosed(1, sampleSize).toArray();
            for (int i : ints) {
                assertTrue(set.add(i));
            }
            assertEquals(sampleSize, set.size());
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

    @Property
    public void testRandomKeys(@InRange(minInt = 0, maxInt = SAMPLE_SIZE_LIMIT) int sampleSize,
            @InRange(minDouble = 0.0, maxDouble = 1.0) double loadFactor)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        for (Class<?> cls : classes) {
            Constructor<?> constructor = cls.getConstructor(int.class, double.class);
            IntSet set = (IntSet) constructor.newInstance(sampleSize, loadFactor);
            // we want a random permutation, not an RNG, to avoid duplicate keys,
            // and the Phi hash has quasi-uniform behavior on sequential integers
            IntHasher hasher = new PhiIntHasher();
            int[] ints = IntStream.rangeClosed(1, sampleSize).map(hasher::hash).toArray();
            for (int i : ints) {
                assertTrue(set.add(i));
            }
            assertEquals(sampleSize, set.size());
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
