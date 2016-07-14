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
package org.gedcom4j.validate;

import java.util.HashSet;
import java.util.List;

/**
 * A class to eliminate duplicate items from a {@link List}, while preserving the order of the List items. If the list
 * contains two or more references to equivalent objects, only the first of these is retained. Assumes that the List of
 * items passed into the constructor is mutable.
 * 
 * 
 * @author frizbog
 * @param <T>
 *            The type of object in the List
 */
public class DuplicateEliminator<T> {

    /**
     * The list of items from which duplicates are to be removed
     */
    private final List<T> items;

    /**
     * Constructor.
     * 
     * @param items
     *            the list of items to remove duplicate entries from.
     */
    public DuplicateEliminator(List<T> items) {
        this.items = items;
    }

    /**
     * Process the list, eliminating duplicates
     * 
     * @return the number of items removed.
     */
    public int process() {
        if (items == null || items.isEmpty()) {
            return 0;
        }
        int result = 0;
        HashSet<T> unique = new HashSet<T>();
        int i = 0;
        while (i < items.size()) {
            T item = items.get(i);
            if (unique.contains(item)) {
                result++;
                items.remove(i);
            } else {
                unique.add(item);
                i++;
            }
        }

        return result;
    }

}
