/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.gedcom4j;

/**
 * A utility class to hold a variety of options for gedcom4j processing.
 * 
 * @author frizbog
 * @since 3.0.0
 */
public final class Options {

    /** Should collections in the object model be pre-initialized? */
    private static boolean collectionInitializationEnabled = false;

    /**
     * Get the collectionInitializationEnabled
     * 
     * @return the collectionInitializationEnabled
     */
    public static boolean isCollectionInitializationEnabled() {
        return collectionInitializationEnabled;
    }

    /**
     * Reset all options to defaults
     */
    public static void resetToDefaults() {
        collectionInitializationEnabled = true;
    }

    /**
     * Set the collectionInitializationEnabled
     * 
     * @param collectionInitializationEnabled
     *            the collectionInitializationEnabled to set
     */
    public static void setCollectionInitializationEnabled(boolean collectionInitializationEnabled) {
        Options.collectionInitializationEnabled = collectionInitializationEnabled;
    }

    /**
     * Private constructor prevents instantiation and subclassing.
     */
    private Options() {
        // Do nothing
    }
}
