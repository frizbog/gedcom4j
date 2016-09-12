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
 * A class to detect or eliminate duplicate items from a {@link List}, while preserving the order of the List items. Can either
 * count the duplicated items, or can remove them. When removing items and the list contains two or more references to equivalent
 * objects, only the first of these is retained. Assumes that the List of items passed into the constructor is mutable.
 * 
 * @author frizbog
 * @param <T>
 *            The type of object in the List
 */
public class DuplicateHandler<T> {

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
    public DuplicateHandler(List<T> items) {
        this.items = items;
    }

    /**
     * Count the duplicates in the list
     * 
     * @return the number of duplicated items counted
     */
    public int count() {
        return process(false);
    }

    /**
     * Remove the duplicates in the list
     * 
     * @return the number of duplicated items remove
     */
    public int remove() {
        return process(true);
    }

    /**
     * Process the list, either counting or eliminating duplicates
     * 
     * @param remove
     *            pass in true if duplicates are to be removed, or false if duplicates are to be merely counted.
     * 
     * @return the number of duplicated items counted or removed
     */
    private int process(boolean remove) {
        if (items == null || items.isEmpty()) {
            return 0;
        }
        int result = 0;
        HashSet<T> unique = new HashSet<>();
        int i = 0;
        while (i < items.size()) {
            T item = items.get(i);
            if (unique.contains(item)) {
                result++;
                if (remove) {
                    items.remove(i);
                } else {
                    i++;
                }
            } else {
                unique.add(item);
                i++;
            }
        }

        return result;
    }

}
