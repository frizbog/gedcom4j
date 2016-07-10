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
package org.gedcom4j.relationship;

/**
 * <p>
 * Names of relationships between individuals, and the names of the inverse of the relationship (which depends on the
 * gender of the original person)
 * </p>
 * .
 *
 * @author frizbog1
 */
public enum RelationshipName {

    /** A father. */
    FATHER(1),
    /** A mother. */
    MOTHER(1),
    /** A husband. */
    HUSBAND(1),
    /** A wife. */
    WIFE(1),
    /** A son. */
    SON(1),
    /** A daughter. */
    DAUGHTER(1),
    /** A child. */
    CHILD(1),
    /** A brother. */
    BROTHER(1),
    /** A sister. */
    SISTER(1),
    /** A sibling. */
    SIBLING(1),

    /** A grandfather. */
    GRANDFATHER(2),
    /** A grandmother. */
    GRANDMOTHER(2),
    /** A grandson. */
    GRANDSON(2),
    /** A granddaughter. */
    GRANDDAUGHTER(2),
    /** A grandchild. */
    GRANDCHILD(2),

    /** A great grandfather. */
    GREAT_GRANDFATHER(3),
    /** A great grandmother. */
    GREAT_GRANDMOTHER(3),
    /** A great grandson. */
    GREAT_GRANDSON(3),
    /** A great granddaughter. */
    GREAT_GRANDDAUGHTER(3),
    /** A great grandchild. */
    GREAT_GRANDCHILD(3),

    /** A great great grandfather. */
    GREAT_GREAT_GRANDFATHER(4),
    /** A great great grandmother. */
    GREAT_GREAT_GRANDMOTHER(4),
    /** A great great grandson. */
    GREAT_GREAT_GRANDSON(4),
    /** A great great granddaughter. */
    GREAT_GREAT_GRANDDAUGHTER(4),
    /** A great great grandchild. */
    GREAT_GREAT_GRANDCHILD(4),

    /** A great great great grandchild. */
    GREAT_GREAT_GREAT_GRANDCHILD(5),
    /** A great great great grandson. */
    GREAT_GREAT_GREAT_GRANDSON(5),
    /** A great great great granddaughter. */
    GREAT_GREAT_GREAT_GRANDDAUGHTER(5),
    /** A great great great grandfather. */
    GREAT_GREAT_GREAT_GRANDFATHER(5),
    /** A great great great grandmother. */
    GREAT_GREAT_GREAT_GRANDMOTHER(5),

    /** A uncle. */
    UNCLE(6),
    /** A aunt. */
    AUNT(6),
    /** A nephew. */
    NEPHEW(6),
    /** A niece. */
    NIECE(6),

    /** A first cousin. */
    FIRST_COUSIN(7),

    /** A great uncle. */
    GREAT_UNCLE(8),
    /** A great aunt. */
    GREAT_AUNT(8),
    /** A great nephew. */
    GREAT_NEPHEW(8),
    /** A great niece. */
    GREAT_NIECE(8),

    /** A great great uncle. */
    GREAT_GREAT_UNCLE(9),
    /** A great great aunt. */
    GREAT_GREAT_AUNT(9),
    /** A great great nephew. */
    GREAT_GREAT_NEPHEW(9),
    /** A great great niece. */
    GREAT_GREAT_NIECE(9);

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

        GREAT_AUNT.reverseForFemale = GREAT_NIECE;
        GREAT_AUNT.reverseForMale = GREAT_NEPHEW;

        GREAT_UNCLE.reverseForFemale = GREAT_NIECE;
        GREAT_UNCLE.reverseForMale = GREAT_NEPHEW;

        GREAT_NEPHEW.reverseForFemale = GREAT_AUNT;
        GREAT_NEPHEW.reverseForMale = GREAT_UNCLE;

        GREAT_NIECE.reverseForFemale = GREAT_AUNT;
        GREAT_NIECE.reverseForMale = GREAT_AUNT;

        GREAT_GREAT_AUNT.reverseForFemale = GREAT_GREAT_NIECE;
        GREAT_GREAT_AUNT.reverseForMale = GREAT_GREAT_NEPHEW;

        GREAT_GREAT_UNCLE.reverseForFemale = GREAT_GREAT_NIECE;
        GREAT_GREAT_UNCLE.reverseForMale = GREAT_GREAT_NEPHEW;

        GREAT_GREAT_NEPHEW.reverseForFemale = GREAT_GREAT_AUNT;
        GREAT_GREAT_NEPHEW.reverseForMale = GREAT_GREAT_UNCLE;

        GREAT_GREAT_NIECE.reverseForFemale = GREAT_GREAT_AUNT;
        GREAT_GREAT_NIECE.reverseForMale = GREAT_GREAT_AUNT;

        FIRST_COUSIN.reverseForFemale = FIRST_COUSIN;
        FIRST_COUSIN.reverseForMale = FIRST_COUSIN;
        FIRST_COUSIN.reverseForUnknown = FIRST_COUSIN;

    }

    /**
     * The simplicity or directness of the relationship. This value is used for evaluating the more preferred/simpler
     * way of expressing relationship chains of equal lengths...all other things being equal, smaller numbers are
     * preferred.
     */
    private final int simplicity;

    /** The reverse of this relationship for males. */
    RelationshipName reverseForMale;

    /** The reverse of this relationship for females. */
    RelationshipName reverseForFemale;

    /** The reverse of this relationship for unknown gender. */
    RelationshipName reverseForUnknown;

    /**
     * Private constructor for setting the simplicity level
     * 
     * @param simplicity
     *            the simplicity level for the relationship being constructed
     */
    private RelationshipName(int simplicity) {
        this.simplicity = simplicity;
    }

    /**
     * Get the simplicity.
     *
     * @return the simplicity
     */
    public int getSimplicity() {
        return simplicity;
    }
}
