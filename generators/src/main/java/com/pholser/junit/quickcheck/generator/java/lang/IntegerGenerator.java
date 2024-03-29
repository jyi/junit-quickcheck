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
 * Produces values of type {@code int} or {@link Integer}.
 */
public class IntegerGenerator extends IntegralGenerator<Integer> {
    private int min = (Integer) defaultValueOf(InRange.class, "minInt");
    private int max = (Integer) defaultValueOf(InRange.class, "maxInt");

    private int seed = (Integer) defaultValueOf(InRange.class, "seedInt");
    private double ratio = (Double) defaultValueOf(InRange.class, "ratioPF");

    private double coeff = (Double) defaultValueOf(InRange.class, "deltaCoeff");
    private int expo = (Integer) defaultValueOf(InRange.class, "deltaExpo");

    @SuppressWarnings("unchecked") public IntegerGenerator() {
        super(asList(Integer.class, int.class));
    }

    /**
     * Tells this generator to produce values within a specified minimum and/or
     * maximum, inclusive, with uniform distribution.
     *
     * {@link InRange#min} and {@link InRange#max} take precedence over
     * {@link InRange#minInt()} and {@link InRange#maxInt()}, if non-empty.
     *
     * @param range annotation that gives the range's constraints
     */
    public void configure(InRange range) {
        useSeed = range.useSeed();
        seed = range.seedInt();
        useRatio = range.useRatio();
        coeff = range.deltaCoeff();
        expo = range.deltaExpo();
//        ratio = range.ratioPF();
        if (useSeed) {
            int delta = (int) (getDelta(seed));
            min = seed - delta;
            max = seed + delta;
        } else {
            min = range.min().isEmpty() ? range.minInt() : Integer.parseInt(range.min());
            max = range.max().isEmpty() ? range.maxInt() : Integer.parseInt(range.max());
        }
    }

    @Override public Integer generate(SourceOfRandomness random, GenerationStatus status) {
        if (useSeed && !seedUsed) {
            seedUsed = true;
            return seed;
        } else {
            return random.nextInt(min, max);
        }
    }


    @Override protected Function<BigInteger, Integer> narrow() {
        return BigInteger::intValue;
    }

    @Override protected Predicate<Integer> inRange() {
        return Comparables.inRange(min, max);
    }

    @Override protected Integer leastMagnitude() {
        return Comparables.leastMagnitude(min, max, 0);
    }

    @Override protected boolean negative(Integer target) {
        return target < 0;
    }

    @Override protected Integer negate(Integer target) {
        return -target;
    }

    @Override public BigDecimal magnitude(Object value) {
        return BigDecimal.valueOf(narrow(value));
    }
}
