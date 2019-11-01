package hash.int32;

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
    @Param({
        IdentityIntHasher.NAME,
        PhiIntHasher.NAME,
        H2IntHasher.NAME,
        Murmur3IntHasher.NAME,
        Prospector2RoundIntHasher.NAME,
        Prospector3RoundIntHasher.NAME,
        // SpeckIntHasher.NAME,
    })
    private String hasherClassName;
    private IntHasher hasher;
    private int randomInteger;

    @Setup(Level.Iteration)
    public void setup() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        this.hasher = (IntHasher) Class.forName(hasherClassName).getDeclaredConstructor().newInstance();
        this.randomInteger = RND.nextInt();
    }

    @Benchmark
    public long measure() {
        return this.hasher.hash(this.randomInteger);
    }
}
