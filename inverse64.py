import sys

# calculate multiplicative inverse of odd number mod 2^64
# from https://groups.google.com/forum/m/#!msg/sci.crypt/UI-UMbUnYGk/hX2-wQVyE3oJ
def inverse(a):
    x = a
    assert (x * a & 0x7) == 1
    x += x - a * x * x
    assert (x * a & 0x3F) == 1
    x += x - a * x * x
    assert (x * a & 0xFFF) == 1
    x += x - a * x * x
    assert (x * a & 0xFFFFFF) == 1
    x += x - a * x * x
    assert (x * a & 0xFFFFFFFF) == 1
    x += x - a * x * x
    assert (x * a & 0xFFFFFFFFFF) == 1
    x += x - a * x * x
    assert (x * a & 0xFFFFFFFFFFFF) == 1
    x += x - a * x * x
    assert (x * a & 0xFFFFFFFFFFFFFF) == 1
    x += x - a * x * x
    assert (x * a & 0xFFFFFFFFFFFFFFFF) == 1
    return x & 0xFFFFFFFFFFFFFFFF


# arg = int(sys.argv[1], 16)
arg = long(sys.argv[1])
# print hex(inverse(arg))
print inverse(arg)
