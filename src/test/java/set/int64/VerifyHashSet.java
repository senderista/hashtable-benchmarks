package set.int64;

import hash.int64.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.LongStream;

@RunWith(JUnitQuickcheck.class)
public class VerifyHashSet {

    private static final int SAMPLE_SIZE_LIMIT = 1 << 20;

    private static final Class<?>[] classes = {
        BLPLongHashSet.class,
        LCFSLongHashSet.class,
        LPLongHashSet.class,
        RHLongHashSet.class,
    };

    @Property
    public void testSequentialKeys(@InRange(minInt = 0, maxInt = SAMPLE_SIZE_LIMIT) int sampleSize,
            @InRange(minDouble = 0.0, maxDouble = 1.0) double loadFactor)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        for (Class<?> cls : classes) {
            Constructor<?> constructor = cls.getConstructor(int.class, double.class);
            LongSet set = (LongSet) constructor.newInstance(sampleSize, loadFactor);
            long[] longs = LongStream.rangeClosed(1, sampleSize).toArray();
            for (long i : longs) {
                assertTrue(set.add(i));
            }
            assertEquals(sampleSize, set.size());
            for (long i : longs) {
                assertTrue(set.contains(i));
            }
            for (long i : longs) {
                assertTrue(set.remove(i));
            }
            assertEquals(0, set.size());
            for (long i : longs) {
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
            LongSet set = (LongSet) constructor.newInstance(sampleSize, loadFactor);
            // we want a random permutation, not an RNG, to avoid duplicate keys,
            // and the Phi hash has quasi-uniform behavior on sequential integers
            LongHasher hasher = new PhiLongHasher();
            long[] longs = LongStream.rangeClosed(1, sampleSize).map(hasher::hash).toArray();
            for (long i : longs) {
                assertTrue(set.add(i));
            }
            assertEquals(sampleSize, set.size());
            for (long i : longs) {
                assertTrue(set.contains(i));
            }
            for (long i : longs) {
                assertTrue(set.remove(i));
            }
            assertEquals(0, set.size());
            for (long i : longs) {
                assertFalse(set.contains(i));
            }
        }
    }
}
