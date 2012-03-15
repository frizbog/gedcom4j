/*
 * Copyright (c) 2009-2012 Matthew R. Harrah
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
package com.mattharrah.gedcom4j.relationship;

/**
 * <p>
 * Names of relationships between individuals. Kept down to minimums for
 * immediate family. More complex relationship names will need to use the
 * RelationshipDescriber class.
 * </p>
 * 
 * @author frizbog1
 */
public enum RelationshipName {
    /**
     * Father
     */
    FATHER,
    /**
     * Mother
     */
    MOTHER,
    /**
     * Husband
     */
    HUSBAND,
    /**
     * Wife
     */
    WIFE,
    /**
     * Son
     */
    SON,
    /**
     * Daughter
     */
    DAUGHTER,
    /**
     * Child - for use when the gender of the child is unknown
     */
    CHILD,
    /**
     * Brother
     */
    BROTHER,
    /**
     * Sister
     */
    SISTER,
    /**
     * Sibling - for use when the gender of the sibling is not known
     */
    SIBLING;
}
