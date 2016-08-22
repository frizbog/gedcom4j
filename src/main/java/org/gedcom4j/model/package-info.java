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
/**
 * <p>
 * A data model for holding genealogical data. Objects are created and populated by classes in the org.gedcom4j.parser package, and
 * written to files using classes in the org.gedcom4j.writer package.
 * </p>
 * <p>
 * There are a large number of {@link java.util.List} member structures in the data model. These can either be automatically
 * initialized on instantiation with empty {@link java.util.ArrayList}s, or left null. The former lets you avoid tedious if-null
 * checks in your code, at the expense of memory (there are a lot of empty lists in most cases). The latter saves memory by not
 * allocating ArrayLists for the list structures, but require you to do null-checking before access.
 * </p>
 * <p>
 * To make this last bit easier, the getters for the List members are overloaded. The normal, no-argument getter simply returns what
 * the backing property refers to - could be an ArrayList, could be null. The overloaded getter takes a boolean parameter, and if
 * true, will initialize the list to an empty ArrayList if it is null before returning the value from the getter. If false, it works
 * exactly the same as the basic no-argument getter.
 * </p>
 * <p>
 * To control whether lists will be pre-initialized and allocated or not, use the
 * {@link org.gedcom4j.Options#setCollectionInitializationEnabled(boolean)} property. Note that this property is not final or
 * thread-safe, so if manipulated during a parse/load operation, strange things could happen.
 * </p>
 * 
 * @author frizbog
 * @since 3.0.0
 */
package org.gedcom4j.model;