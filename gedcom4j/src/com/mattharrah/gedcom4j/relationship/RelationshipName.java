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
 * Names of relationships between individuals, and the names of the inverse of
 * the relationship (which depends on the gender of the original person)
 * </p>
 * 
 * @author frizbog1
 */
public enum RelationshipName {

    /* CHECKSTYLE:OFF */
    FATHER, MOTHER, HUSBAND, WIFE, SON, DAUGHTER, CHILD, BROTHER, SISTER,
    SIBLING, GRANDFATHER, GRANDMOTHER, GRANDSON, GRANDDAUGHTER, GRANDCHILD,
    GREAT_GRANDFATHER, GREAT_GRANDMOTHER, GREAT_GRANDSON, GREAT_GRANDDAUGHTER,
    GREAT_GRANDCHILD, GREAT_GREAT_GRANDFATHER, GREAT_GREAT_GRANDMOTHER,
    GREAT_GREAT_GRANDSON, GREAT_GREAT_GRANDDAUGHTER, GREAT_GREAT_GRANDCHILD,
    GREAT_GREAT_GREAT_GRANDCHILD, GREAT_GREAT_GREAT_GRANDSON,
    GREAT_GREAT_GREAT_GRANDDAUGHTER, GREAT_GREAT_GREAT_GRANDFATHER,
    GREAT_GREAT_GREAT_GRANDMOTHER,

    UNCLE, AUNT, NEPHEW, NIECE;
    /* CHECKSTYLE:ON */

    /**
     * The reverse of this relationship for males
     */
    RelationshipName reverseForMale;
    /**
     * The reverse of this relationship for females
     */
    RelationshipName reverseForFemale;
    /**
     * The reverse of this relationship for unknown gender
     */
    RelationshipName reverseForUnknown;

    static {
        /* Load all the reverse relationships */

        // Not being homophobic, but GEDCOM doesn't support anything but
        // heterosexual marriage
        HUSBAND.reverseForFemale = WIFE;
        WIFE.reverseForMale = HUSBAND;

        FATHER.reverseForMale = SON;
        FATHER.reverseForFemale = DAUGHTER;
        FATHER.reverseForUnknown = CHILD;

        MOTHER.reverseForMale = SON;
        MOTHER.reverseForFemale = DAUGHTER;
        MOTHER.reverseForUnknown = CHILD;

        SON.reverseForMale = FATHER;
        SON.reverseForFemale = MOTHER;

        DAUGHTER.reverseForMale = FATHER;
        DAUGHTER.reverseForFemale = MOTHER;

        CHILD.reverseForMale = FATHER;
        CHILD.reverseForFemale = MOTHER;

        BROTHER.reverseForMale = BROTHER;
        BROTHER.reverseForFemale = SISTER;
        BROTHER.reverseForUnknown = SIBLING;

        SISTER.reverseForMale = BROTHER;
        SISTER.reverseForFemale = SISTER;
        SISTER.reverseForUnknown = SIBLING;

        SIBLING.reverseForMale = BROTHER;
        SIBLING.reverseForFemale = SISTER;
        SIBLING.reverseForUnknown = SIBLING;

        GRANDFATHER.reverseForMale = GRANDSON;
        GRANDFATHER.reverseForFemale = GRANDDAUGHTER;
        GRANDFATHER.reverseForUnknown = GRANDCHILD;

        GRANDMOTHER.reverseForMale = GRANDSON;
        GRANDMOTHER.reverseForFemale = GRANDDAUGHTER;
        GRANDMOTHER.reverseForUnknown = GRANDCHILD;

        GRANDSON.reverseForMale = GRANDFATHER;
        GRANDSON.reverseForFemale = GRANDMOTHER;

        GRANDDAUGHTER.reverseForMale = GRANDFATHER;
        GRANDDAUGHTER.reverseForFemale = GRANDMOTHER;

        GRANDCHILD.reverseForMale = GRANDFATHER;
        GRANDCHILD.reverseForFemale = GRANDMOTHER;

        GREAT_GRANDFATHER.reverseForMale = GREAT_GRANDSON;
        GREAT_GRANDFATHER.reverseForFemale = GREAT_GRANDDAUGHTER;
        GREAT_GRANDFATHER.reverseForUnknown = GREAT_GRANDCHILD;

        GREAT_GRANDMOTHER.reverseForMale = GREAT_GRANDSON;
        GREAT_GRANDMOTHER.reverseForFemale = GREAT_GRANDDAUGHTER;
        GREAT_GRANDMOTHER.reverseForUnknown = GREAT_GRANDCHILD;

        GREAT_GRANDSON.reverseForMale = GREAT_GRANDFATHER;
        GREAT_GRANDSON.reverseForFemale = GREAT_GRANDMOTHER;

        GREAT_GRANDDAUGHTER.reverseForMale = GREAT_GRANDFATHER;
        GREAT_GRANDDAUGHTER.reverseForFemale = GREAT_GRANDMOTHER;

        GREAT_GRANDCHILD.reverseForMale = GREAT_GRANDFATHER;
        GREAT_GRANDCHILD.reverseForFemale = GREAT_GRANDMOTHER;

        GREAT_GREAT_GRANDFATHER.reverseForMale = GREAT_GREAT_GRANDSON;
        GREAT_GREAT_GRANDFATHER.reverseForFemale = GREAT_GREAT_GRANDDAUGHTER;
        GREAT_GREAT_GRANDFATHER.reverseForUnknown = GREAT_GREAT_GRANDCHILD;

        GREAT_GREAT_GRANDMOTHER.reverseForMale = GREAT_GREAT_GRANDSON;
        GREAT_GREAT_GRANDMOTHER.reverseForFemale = GREAT_GREAT_GRANDDAUGHTER;
        GREAT_GREAT_GRANDMOTHER.reverseForUnknown = GREAT_GREAT_GRANDCHILD;

        GREAT_GREAT_GRANDSON.reverseForMale = GREAT_GREAT_GRANDFATHER;
        GREAT_GREAT_GRANDSON.reverseForFemale = GREAT_GREAT_GRANDMOTHER;

        GREAT_GREAT_GRANDDAUGHTER.reverseForMale = GREAT_GREAT_GRANDFATHER;
        GREAT_GREAT_GRANDDAUGHTER.reverseForFemale = GREAT_GREAT_GRANDMOTHER;

        GREAT_GREAT_GRANDCHILD.reverseForMale = GREAT_GREAT_GRANDFATHER;
        GREAT_GREAT_GRANDCHILD.reverseForFemale = GREAT_GREAT_GRANDMOTHER;

        GREAT_GREAT_GREAT_GRANDCHILD.reverseForFemale = GREAT_GREAT_GREAT_GRANDMOTHER;
        GREAT_GREAT_GREAT_GRANDCHILD.reverseForMale = GREAT_GREAT_GREAT_GRANDFATHER;

        GREAT_GREAT_GREAT_GRANDSON.reverseForFemale = GREAT_GREAT_GREAT_GRANDMOTHER;
        GREAT_GREAT_GREAT_GRANDSON.reverseForMale = GREAT_GREAT_GREAT_GRANDFATHER;

        GREAT_GREAT_GREAT_GRANDDAUGHTER.reverseForFemale = GREAT_GREAT_GREAT_GRANDMOTHER;
        GREAT_GREAT_GREAT_GRANDDAUGHTER.reverseForMale = GREAT_GREAT_GREAT_GRANDFATHER;

        GREAT_GREAT_GREAT_GRANDMOTHER.reverseForFemale = GREAT_GREAT_GREAT_GRANDDAUGHTER;
        GREAT_GREAT_GREAT_GRANDMOTHER.reverseForMale = GREAT_GREAT_GREAT_GRANDSON;
        GREAT_GREAT_GREAT_GRANDMOTHER.reverseForUnknown = GREAT_GREAT_GREAT_GRANDCHILD;

        GREAT_GREAT_GREAT_GRANDFATHER.reverseForFemale = GREAT_GREAT_GREAT_GRANDDAUGHTER;
        GREAT_GREAT_GREAT_GRANDFATHER.reverseForMale = GREAT_GREAT_GREAT_GRANDSON;
        GREAT_GREAT_GREAT_GRANDFATHER.reverseForUnknown = GREAT_GREAT_GREAT_GRANDCHILD;

        UNCLE.reverseForMale = NEPHEW;
        UNCLE.reverseForFemale = NIECE;

        AUNT.reverseForMale = NEPHEW;
        AUNT.reverseForFemale = NIECE;

        NEPHEW.reverseForMale = UNCLE;
        NEPHEW.reverseForFemale = AUNT;

        NIECE.reverseForMale = UNCLE;
        NIECE.reverseForFemale = AUNT;

    }
}
