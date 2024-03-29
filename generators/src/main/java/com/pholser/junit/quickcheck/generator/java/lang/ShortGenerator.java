/*
 The MIT License

 Copyright (c) 2010-2018 Paul R. Holser, Jr.

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.pholser.junit.quickcheck.generator.java.lang;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;
import java.util.function.Predicate;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.IntegralGenerator;
import com.pholser.junit.quickcheck.internal.Comparables;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.util.Arrays.*;

import static com.pholser.junit.quickcheck.internal.Reflection.*;

/**
 * Produces values of type {@code short} or {@link Short}.
 */
public class ShortGenerator extends IntegralGenerator<Short> {
    private short min = (Short) defaultValueOf(InRange.class, "minShort");
    private short max = (Short) defaultValueOf(InRange.class, "maxShort");

    private short seed = (Short) defaultValueOf(InRange.class, "seedShort");


    @SuppressWarnings("unchecked") public ShortGenerator() {
        super(asList(Short.class, short.class));
    }

    /**
     * Tells this generator to produce values within a specified minimum and/or
     * maximum, inclusive, with uniform distribution.
     *
     * {@link InRange#min} and {@link InRange#max} take precedence over
     * {@link InRange#minShort()} and {@link InRange#maxShort()}, if non-empty.
     *
     * @param range annotation that gives the range's constraints
     */
    public void configure(InRange range) {
        useSeed = range.useSeed();
        seed = range.seedShort();
        if (useSeed) {
            short delta = (short) getDelta((int) seed);
            min = (short) (seed - delta);
            max = (short) (seed + delta);
        } else {
            min = range.min().isEmpty() ? range.minShort() : Short.parseShort(range.min());
            max = range.max().isEmpty() ? range.maxShort() : Short.parseShort(range.max());
        }
    }

    @Override public Short generate(SourceOfRandomness random, GenerationStatus status) {
        if (useSeed && !seedUsed) {
            seedUsed = true;
            return seed;
        } else {
            return random.nextShort(min, max);
        }
    }

    @Override protected Function<BigInteger, Short> narrow() {
        return BigInteger::shortValue;
    }

    @Override protected Predicate<Short> inRange() {
        return Comparables.inRange(min, max);
    }

    @Override protected Short leastMagnitude() {
        return Comparables.leastMagnitude(min, max, (short) 0);
    }

    @Override protected boolean negative(Short target) {
        return target < 0;
    }

    @Override protected Short negate(Short target) {
        return (short) -target;
    }

    @Override public BigDecimal magnitude(Object value) {
        return BigDecimal.valueOf(narrow(value));
    }
}
