# hashtable-benchmarks

## An Evaluation of Linear Probing Hashtable Algorithms
This repository contains implementations, unit tests, simulation code, and benchmark code for 4 linear probing algorithms: standard linear probing, <a href="https://doi.org/10.1016/0196-6774(89)90014-X">Last-Come First-Served</a> (LCFS), <a href="https://doi.org/10.1109/SFCS.1985.48">Robin Hood</a> (RH), and <a href="https://doi.org/10.1093/comjnl/17.2.135">Bidirectional Linear Probing</a> (BLP). The latter was published by Knuth in 1973 but has received little attention since. It outperforms all other linear probing variants tested, including the recently popular "Robin Hood" variant.

All implementations are in Java, and are benchmarked using the <a href="http://openjdk.java.net/projects/code-tools/jmh/">JMH</a> benchmark framework.
