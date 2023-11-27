# hashtable-benchmarks

## An Evaluation of Linear Probing Hashtable Algorithms
This repository contains implementations, unit and property tests, and benchmark code for 4 linear probing algorithms: standard linear probing, <a href="https://doi.org/10.1016/0196-6774(89)90014-X">Last-Come First-Served</a> (LCFS), <a href="https://doi.org/10.1109/SFCS.1985.48">Robin Hood</a> (RH), and <a href="https://doi.org/10.1093/comjnl/17.2.135">Bidirectional Linear Probing</a> (BLP). The latter was published by Knuth in 1973 but has received little attention since. It outperforms all other linear probing variants tested, including the recently popular "Robin Hood" variant.

The current implementations only accept nonzero 32- or 64-bit integer keys, with no values (I use invertible hash functions, so there's no need to separately store hash codes). Deletions are tombstone-free, so there's no need to rehash after several deletions. Dynamic resizing is not currently supported (I have a separate project on incrementally resizing open-addressed hash tables which isn't ready to publish yet). I plan to implement `IntInt`, `LongLong`, `IntLong`, `LongInt` maps at some point. Eventually I want to implement a generic Java hash table using bidirectional linear probing and mapping hash codes to offsets in an array of object references (similar to CPython's `dict` implementation), but it will likely be a while before I have time for this.

Additionally, I've collected a number of 32- and 64-bit invertible hash functions which may be of independent interest (I had to calculate most of the inverses myself, using the scripts https://github.com/senderista/hashtable-benchmarks/blob/master/inverse32.py and https://github.com/senderista/hashtable-benchmarks/blob/master/inverse64.py). The implementations can be found in the https://github.com/senderista/hashtable-benchmarks/tree/master/src/main/java/hash/int32 and https://github.com/senderista/hashtable-benchmarks/tree/master/src/main/java/hash/int64 directories. (I started work on a cryptographically strong invertible hash function based on the Speck cipher as a simulation baseline, but didn't complete it, since simulation results didn't seem as practically important as performance results.)

All implementations are in Java, and are benchmarked using the <a href="http://openjdk.java.net/projects/code-tools/jmh/">JMH</a> benchmark framework.

You can run property-based tests from the repository root directory by typing `gradle test`, or benchmarks by typing `gradle jmh`.

Javadoc (with extensive documentation of the algorithms involved) is here:
https://senderista.github.io/hashtable-benchmarks/.

Benchmark results are here:

https://github.com/senderista/hashtable-benchmarks/wiki/32-bit-benchmarks

https://github.com/senderista/hashtable-benchmarks/wiki/64-bit-benchmarks
