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

import java.util.List;

/**
 * A class to detect or eliminate null items from a {@link List}, while preserving the order of the List items. Can either count the
 * null items, or can remove them. Assumes that the List of items passed into the constructor is mutable.
 * 
 * @author frizbog
 * @param <T>
 *            The type of object in the List
 */
public class NullHandler<T> {

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
    public NullHandler(List<T> items) {
        this.items = items;
    }

    /**
     * Count the nulls in the list
     * 
     * @return the number of null items counted
     */
    public int count() {
        return process(false);
    }

    /**
     * Remove the nulls from the list
     * 
     * @return the number of null items removed
     */
    public int remove() {
        return process(true);
    }

    /**
     * Process the list, either counting or eliminating duplicates
     * 
     * @param remove
     *            pass in true if nulls are to be removed, or false if nulls are to be merely counted.
     * 
     * @return the number of null items counted or removed
     */
    private int process(boolean remove) {
        if (items == null || items.isEmpty()) {
            return 0;
        }
        int result = 0;
        int i = 0;
        while (i < items.size()) {
            T item = items.get(i);
            if (item == null) {
                result++;
                if (remove) {
                    items.remove(i);
                } else {
                    i++;
                }
            } else {
                i++;
            }
        }

        return result;
    }

}
