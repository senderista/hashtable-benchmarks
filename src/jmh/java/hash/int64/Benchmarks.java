package hash.int64;

import org.openjdk.jmh.annotations.*;

import java.lang.reflect.InvocationTargetException;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import java.security.SecureRandom;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
public class Benchmarks {
    private static final Random RND = new SecureRandom();
    @Param({ IdentityLongHasher.NAME, PhiLongHasher.NAME, DegskiLongHasher.NAME, Murmur3LongHasher.NAME,
            Variant13LongHasher.NAME, WangLongHasher.NAME,
            // SpeckLongHasher.NAME,
    })
    private String hasherClassName;
    private LongHasher hasher;
    private long randomLong;

    @Setup(Level.Iteration)
    public void setup() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        this.hasher = (LongHasher) Class.forName(hasherClassName).getDeclaredConstructor().newInstance();
        this.randomLong = RND.nextLong();
    }

    @Benchmark
    public long measure() {
        return this.hasher.hash(this.randomLong);
    }
}
