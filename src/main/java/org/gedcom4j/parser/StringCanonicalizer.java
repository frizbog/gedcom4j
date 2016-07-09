/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package org.gedcom4j.parser;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A class to handle canonical strings, to reduce memory consumption by repeated instances of frequently used string
 * values. Unlike string.intern(), this implementation does not put things in permgen (or does it)? It also has a max
 * size, only canonicalizes strings repeated more than a certain number of times.
 * 
 * @author frizbog
 *
 */
class StringCanonicalizer {

    /**
     * A canonicalized string
     * 
     * @author frizbog
     */
    class CanonicalizedString {
        /**
         * The number of times this entry is used
         */
        long count;

        /**
         * The value of this entry
         */
        final String value;

        /**
         * Constructor
         * 
         * @param str
         *            the string value
         */
        public CanonicalizedString(String str) {
            value = str;
            count = 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("CanonicalizedString [count=");
            builder.append(count);
            builder.append(", ");
            if (value != null) {
                builder.append("value=");
                builder.append(value);
            }
            builder.append("]");
            return builder.toString();
        }

    }

    /**
     * The string pool
     */
    final ConcurrentMap<String, CanonicalizedString> stringPool = new ConcurrentHashMap<String, CanonicalizedString>(100);

    /**
     * The minimum number of usages that a canonicalized string has to have to remain in the pool during cleanup
     */
    private int poolUsageThreshold = 25;

    /**
     * The max size of the string pool
     */
    private int maxPoolSize = 1000;

    /**
     * The number of eviction events that occurred
     */
    int numEvictions = 0;

    /**
     * The number of items evicted
     */
    int numEvictedCumulative = 0;

    /**
     * A flag to indicate whether we've maxed out the pool to keep from thrashing
     */
    boolean maxedOut = false;

    /**
     * Default constructor
     */
    public StringCanonicalizer() {
        this(500, 25);
    }

    /**
     * Constructor that lets you specify the pool size and usage threshold
     * 
     * @param maxPoolSize
     *            the maximum number of items in the pool
     * @param poolUsageThreshold
     *            the minimum number of uses to remain in the pool during an eviction
     */
    public StringCanonicalizer(int maxPoolSize, int poolUsageThreshold) {
        this.maxPoolSize = maxPoolSize;
        this.poolUsageThreshold = poolUsageThreshold;
    }

    /**
     * Get the maximum number of items in the pool
     * 
     * @return the maximum number of items in the pool
     */
    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    /**
     * Get the cumulative total count of items that have been evicted from the pool
     * 
     * @return the cumulative total count of items that have been evicted from the pool
     */
    public int getNumEvictedCumulative() {
        return numEvictedCumulative;
    }

    /**
     * Get the number of times the pool has been evicted
     * 
     * @return the number of times the pool has been evicted
     */
    public int getNumEvictions() {
        return numEvictions;
    }

    /**
     * Get the minimum usage to remain in the pool after eviction
     * 
     * @return the minimum usage to remain in the pool after eviction
     */
    public int getPoolUsageThreshold() {
        return poolUsageThreshold;
    }

    /**
     * Is the pool maxed out? Meaning - is it full, and evictions aren't helping?
     * 
     * @return is the pool maxed out?
     */
    public boolean isMaxedOut() {
        return maxedOut;
    }

    /**
     * Get the canonical version of a string
     * 
     * @param str
     *            the string we want to get a canonical version of
     * @return either the string itself, or the canonicalized version, depending on the state of the string pool
     */
    String getCanonicalVersion(String str) {
        if (str == null) {
            return str;
        }
        if (str.length() == 0) {
            return "".intern();
        }
        CanonicalizedString canon;
        canon = stringPool.get(str);
        if (canon != null) {
            canon.count++;
            return canon.value;
        }

        // Pool is too big - try and evict the lowest used entries
        if (stringPool.size() > maxPoolSize) {
            evictLowValueEntries();
            // If evicting didn't help, return the original string and mark the pool as maxed out to prevent thrashing
            if (stringPool.size() > maxPoolSize) {
                maxedOut = true;
                return str;
            }
        }
        canon = new CanonicalizedString(str);
        stringPool.put(str, canon);
        return canon.value;
    }

    /**
     * Clear the string pool entirely
     */
    void reset() {
        stringPool.clear();
        numEvictedCumulative = 0;
        numEvictions = 0;
        maxedOut = false;
    }

    /**
     * Evict all items that have fewer than uses
     */
    private void evictLowValueEntries() {
        if (maxedOut) {
            return;
        }
        numEvictions++;
        int before = stringPool.size();
        for (CanonicalizedString canon : stringPool.values()) {
            if (canon.count < poolUsageThreshold) {
                stringPool.remove(canon.value);
            }
        }
        int after = stringPool.size();
        numEvictedCumulative += (before - after);
        if ((before - after) < 2) {
            maxedOut = true;
        }
        // System.out.println((before - after) + " item(s) evicted from string pool - was " + before + ", now " +
        // after);
    }

}
