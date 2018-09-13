// originally copied from https://raw.githubusercontent.com/vigna/fastutil/master/src/it/unimi/dsi/fastutil/HashCommon.java

/*
 * Copyright (C) 2002-2017 Sebastiano Vigna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/** Common code for all hash-based classes. */

public class Utils {

    protected Utils() {}

    /** Returns the hash code that would be returned by {@link Float#hashCode()}.
     *
     * @param f a float.
     * @return the same code as {@link Float#hashCode() new Float(f).hashCode()}.
     */

    public static int float2int(final float f) {
        return Float.floatToRawIntBits(f);
    }

    /** Returns the hash code that would be returned by {@link Double#hashCode()}.
     *
     * @param d a double.
     * @return the same code as {@link Double#hashCode() new Double(f).hashCode()}.
     */

    public static int double2int(final double d) {
        final long l = Double.doubleToRawLongBits(d);
        return (int)(l ^ (l >>> 32));
    }

    /** Returns the hash code that would be returned by {@link Long#hashCode()}.
     *
     * @param l a long.
     * @return the same code as {@link Long#hashCode() new Long(f).hashCode()}.
     */
    public static int long2int(final long l) {
        return (int)(l ^ (l >>> 32));
    }

    /** Returns the least power of two greater than or equal to the specified value.
     *
     * <p>Note that this function will return 1 when the argument is 0.
     *
     * @param x an integer smaller than or equal to 2<sup>30</sup>.
     * @return the least power of two greater than or equal to the specified value.
     */
    public static int nextPowerOfTwo(int x) {
        if (x == 0) return 1;
        x--;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        return (x | x >> 16) + 1;
    }

    /** Returns the least power of two greater than or equal to the specified value.
     *
     * <p>Note that this function will return 1 when the argument is 0.
     *
     * @param x a long integer smaller than or equal to 2<sup>62</sup>.
     * @return the least power of two greater than or equal to the specified value.
     */
    public static long nextPowerOfTwo(long x) {
        if (x == 0) return 1;
        x--;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return (x | x >> 32) + 1;
    }

    /** Returns the maximum number of entries that can be filled before rehashing.
     *
     * @param n the size of the backing array.
     * @param f the load factor.
     * @return the maximum number of entries before rehashing.
     */
    public static int maxFill(final int n, final float f) {
        /* We must guarantee that there is always at least
         * one free entry (even with pathological load factors). */
        return Math.min((int)Math.ceil(n * f), n - 1);
    }

    /** Returns the maximum number of entries that can be filled before rehashing.
     *
     * @param n the size of the backing array.
     * @param f the load factor.
     * @return the maximum number of entries before rehashing.
     */
    public static long maxFill(final long n, final float f) {
        /* We must guarantee that there is always at least
         * one free entry (even with pathological load factors). */
        return Math.min((long)Math.ceil(n * f), n - 1);
    }

    /** Returns the least power of two smaller than or equal to 2<sup>30</sup> and larger than or equal to {@code Math.ceil(expected / f)}.
     *
     * @param expected the expected number of elements in a hash table.
     * @param f the load factor.
     * @return the minimum possible size for a backing array.
     * @throws IllegalArgumentException if the necessary size is larger than 2<sup>30</sup>.
     */
    public static int arraySize(final int expected, final float f) {
        final long s = Math.max(2, nextPowerOfTwo((long)Math.ceil(expected / f)));
        if (s > (1 << 30)) throw new IllegalArgumentException("Too large (" + expected + " expected elements with load factor " + f + ")");
        return (int)s;
    }

    /** Returns the least power of two larger than or equal to {@code Math.ceil(expected / f)}.
     *
     * @param expected the expected number of elements in a hash table.
     * @param f the load factor.
     * @return the minimum possible size for a backing big array.
     */
    public static long bigArraySize(final long expected, final float f) {
        return nextPowerOfTwo((long)Math.ceil(expected / f));
    }
}
