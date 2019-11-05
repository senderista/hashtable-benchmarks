package set.int64;

import hash.int64.LongHasher;
import hash.int64.PhiLongHasher;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;
import java.lang.reflect.InvocationTargetException;

import org.openjdk.jmh.annotations.*;

public class Benchmarks {

    private static final int FORKS = 1;
    private static final int ITERATIONS = 5;
    private static final int SINGLE_SHOT_MULTIPLIER = 1;
    private static final int BATCH_SIZE = 1000;

    // implements Fisher–Yates shuffle over range of array, inexplicably missing
    // from java.util.Arrays
    private static void shuffleArray(long[] arr, int start, int end) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = end - start - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int randomIndex = start + index;
            int currentIndex = start + i;
            assert start <= randomIndex && randomIndex < end;
            assert start <= currentIndex && currentIndex < end;
            // Simple swap
            long a = arr[randomIndex];
            arr[randomIndex] = arr[currentIndex];
            arr[currentIndex] = a;
        }
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        @Param({
            LPLongHashSet.NAME,
            LCFSLongHashSet.NAME,
            RHLongHashSet.NAME,
            BLPLongHashSet.NAME,
        })
        private String setClassName;

        @Param({
            "10000",
            "100000",
            "1000000",
            "10000000",
            "100000000",
        })
        private int setSize;

        @Param({
            "0.5",
            "0.75",
            "0.9",
            "0.95",
            "0.99",
            // "1.0",
        })
        private double loadFactor;

        public LongSet hashSetTemplate;
        public long[] newTestData;
        public long[] oldTestData;
        // we want a random permutation on test data, not an RNG, to avoid duplicate
        // keys, and the Phi hash has quasi-uniform behavior on sequential integers
        private final LongHasher hasher = new PhiLongHasher();

        @Setup(Level.Trial)
        public void initBenchmarkState() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
                NoSuchMethodException, InvocationTargetException {
            // generate array of random ints, using random permutation rather than RNG
            // to avoid duplicates.
            long[] testData = LongStream.rangeClosed(1, setSize).map(hasher::hash).toArray();
            // populate hash set under test with all test data except the last BATCH_SIZE elements,
            // to leave some data free for testing deletions and unsuccessful lookups.
            int templateSize = testData.length - BATCH_SIZE;
            this.hashSetTemplate = (LongSet) Class.forName(setClassName).getDeclaredConstructor(int.class, double.class)
                    .newInstance(setSize, loadFactor);
            for (int i = 0; i < templateSize; ++i) {
                this.hashSetTemplate.add(testData[i]);
            }
            // now generate a random sample of BATCH_SIZE test data that are not present in the hash map
            this.newTestData = Arrays.copyOfRange(testData, testData.length - BATCH_SIZE, testData.length);
            shuffleArray(testData, 0, templateSize);
            this.oldTestData = Arrays.copyOfRange(testData, 0, BATCH_SIZE);
        }
    }

    @State(Scope.Thread)
    public static class IterationState {
        public LongSet hashSet;
        public int testDataIndex;

        public int getDataIndex() {
            int ret;
            if (this.testDataIndex == BATCH_SIZE) {
                ret = 0;
            } else {
                ret = this.testDataIndex;
                ++this.testDataIndex;
            }
            assert this.testDataIndex <= BATCH_SIZE;
            return ret;
        }

        @Setup(Level.Iteration)
        public void initIterationState(BenchmarkState bs) throws CloneNotSupportedException {
            this.hashSet = bs.hashSetTemplate.cloneSet();
            this.testDataIndex = 0;
        }

        @TearDown(Level.Iteration)
        public void destroyIterationState() {
            this.hashSet.clear();
            assert this.testDataIndex <= BATCH_SIZE;
        }
    }

    @Benchmark
    @Fork(FORKS)
    @Warmup(iterations = ITERATIONS * SINGLE_SHOT_MULTIPLIER, batchSize = BATCH_SIZE)
    @Measurement(iterations = ITERATIONS * SINGLE_SHOT_MULTIPLIER, batchSize = BATCH_SIZE)
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public boolean measureAdd(BenchmarkState bs, IterationState is) {
        boolean notPresent = is.hashSet.add(bs.newTestData[is.getDataIndex()]);
        assert notPresent;
        return notPresent;
    }

    @Benchmark
    @Fork(FORKS)
    @Warmup(iterations = ITERATIONS * SINGLE_SHOT_MULTIPLIER, batchSize = BATCH_SIZE)
    @Measurement(iterations = ITERATIONS * SINGLE_SHOT_MULTIPLIER, batchSize = BATCH_SIZE)
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public boolean measureRemove(BenchmarkState bs, IterationState is) {
        boolean present = is.hashSet.remove(bs.oldTestData[is.getDataIndex()]);
        assert present;
        return present;
    }

    @Benchmark
    @Fork(FORKS)
    @Warmup(iterations = ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @BenchmarkMode({Mode.AverageTime, Mode.SampleTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public boolean measureSuccessfulContains(BenchmarkState bs, IterationState is) {
        boolean present = is.hashSet.contains(bs.oldTestData[is.getDataIndex()]);
        assert present;
        return present;
    }

    @Benchmark
    @Fork(FORKS)
    @Warmup(iterations = ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @BenchmarkMode({Mode.AverageTime, Mode.SampleTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public boolean measureUnsuccessfulContains(BenchmarkState bs, IterationState is) {
        boolean present = is.hashSet.contains(bs.newTestData[is.getDataIndex()]);
        assert !present;
        return present;
    }
}
