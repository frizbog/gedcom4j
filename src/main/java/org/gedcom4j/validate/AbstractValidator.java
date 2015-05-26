/*
 * Copyright (c) 2009-2014 Matthew R. Harrah
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
package org.gedcom4j.validate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.model.*;

/**
 * A base class for all validators
 * 
 * @author frizbog1
 * 
 */
abstract class AbstractValidator {

    /**
     * The root validator - the one that holds the collection of findings among other things. Must be declared
     * specifically a {@link GedcomValidator} and not an {@link AbstractValidator}
     */
    protected GedcomValidator rootValidator;

    /**
     * Add a new finding of severity ERROR
     * 
     * @param description
     *            the description of the error
     */
    protected void addError(String description) {
        rootValidator.findings.add(new GedcomValidationFinding(description, Severity.ERROR, null));
    }

    /**
     * Add a new finding of severity ERROR
     * 
     * @param description
     *            the description of the error
     * @param o
     *            the object in error
     */
    protected void addError(String description, Object o) {
        rootValidator.findings.add(new GedcomValidationFinding(description, Severity.ERROR, o));
    }

    /**
     * Add a new finding of severity INFO
     * 
     * @param description
     *            the description of the finding
     */
    protected void addInfo(String description) {
        rootValidator.findings.add(new GedcomValidationFinding(description, Severity.INFO, null));
    }

    /**
     * Add a new finding of severity INFO
     * 
     * @param description
     *            the description of the finding
     * @param o
     *            the object in error
     */
    protected void addInfo(String description, Object o) {
        rootValidator.findings.add(new GedcomValidationFinding(description, Severity.INFO, o));
    }

    /**
     * Add a new finding of severity WARNING
     * 
     * @param description
     *            the description of the warning
     */
    protected void addWarning(String description) {
        rootValidator.findings.add(new GedcomValidationFinding(description, Severity.WARNING, null));
    }

    /**
     * Add a new finding of severity WARNING
     * 
     * @param description
     *            the description of the warning
     * @param o
     *            the object in error
     */
    protected void addWarning(String description, Object o) {
        rootValidator.findings.add(new GedcomValidationFinding(description, Severity.WARNING, o));
    }

    /**
     * Check a change date structure
     * 
     * @param changeDate
     *            the change date structure to check
     * @param objectWithChangeDate
     *            the object with the change date
     */
    protected void checkChangeDate(ChangeDate changeDate, Object objectWithChangeDate) {
        if (changeDate == null) {
            // Change dates are always optional
            return;
        }
        checkRequiredString(changeDate.date, "change date", objectWithChangeDate);
        checkOptionalString(changeDate.time, "change time", objectWithChangeDate);
        if (changeDate.notes == null) {
            if (rootValidator.autorepair) {
                changeDate.notes = new ArrayList<Note>();
                addInfo("Notes collection was null on " + changeDate.getClass().getSimpleName() + " - autorepaired");
            } else {
                addError("Notes collection is null on " + changeDate.getClass().getSimpleName());
            }
        } else {
            checkNotes(changeDate.notes, changeDate);
        }

    }

    /**
     * Check custom tags on an object. Uses reflection to look for a property named "customTags" and checks if it's
     * null--it's supposed to be at least an instantiated and empty collection. If autorepair is on, it will
     * reflectively fix this.
     * 
     * @param o
     *            the object being validated
     */
    protected void checkCustomTags(Object o) {

        Field customTagsField = null;
        try {
            customTagsField = o.getClass().getField("customTags");
        } catch (NoSuchFieldException e) {
            addError("There is no field named 'customTags' on object of type " + o.getClass().getSimpleName() + "", o);
            return;
        } catch (SecurityException e) {
            addError("There is no field named 'customTags' on object of type " + o.getClass().getSimpleName() + "", o);
            return;
        }

        Object fldVal = null;
        try {
            fldVal = customTagsField.get(o);
        } catch (IllegalArgumentException e) {
            addError("Cannot get value of customTags attribute on object of type " + o.getClass().getSimpleName()
                    + " - " + e.getMessage(), o);
            return;
        } catch (IllegalAccessException e) {
            addError("Cannot get value of customTags attribute on object of type " + o.getClass().getSimpleName()
                    + " - " + e.getMessage(), o);
            return;
        }
        if (fldVal == null) {
            if (rootValidator.autorepair) {
                List<StringTree> customTags = new ArrayList<StringTree>();
                try {
                    customTagsField.set(o, customTags);
                } catch (IllegalArgumentException e) {
                    addError("Cannot autorepair value of customTags attribute on object of type "
                            + o.getClass().getSimpleName() + " - " + e.getMessage(), o);
                    return;
                } catch (IllegalAccessException e) {
                    addError("Cannot autorepair value of customTags attribute on object of type "
                            + o.getClass().getSimpleName() + " - " + e.getMessage(), o);
                    return;
                }
                rootValidator.addInfo("Custom tag collection was null - repaired", o);
            } else {
                rootValidator.addError("Custom tag collection is null - must be at least an empty collection", o);
            }
        } else {
            if (!(fldVal instanceof List<?>)) {
                rootValidator.addError("Custom tag collection is not a List", o);
            }
        }
    }

    /**
     * Check a notes collection
     * 
     * @param notes
     *            the notes collection
     * @param objectWithNotes
     *            the object that has notes
     */
    protected void checkNotes(List<Note> notes, Object objectWithNotes) {
        new NotesValidator(rootValidator, objectWithNotes, notes).validate();
    }

    /**
     * Checks that the value for an optional tag is either null, or greater than zero characters long after trimming.
     * 
     * @param optionalString
     *            the field that is required
     * @param fieldDescription
     *            the human-readable name of the field
     * @param objectContainingField
     *            the object containing the field being checked
     */
    protected void checkOptionalString(String optionalString, String fieldDescription, Object objectContainingField) {
        if (optionalString != null && optionalString.trim().length() == 0) {
            addError(fieldDescription + " on " + objectContainingField.getClass().getSimpleName()
                    + " is specified, but has a blank value", objectContainingField);
        }
    }

    /**
     * Checks that the value for an optional tag is either null, or greater than zero characters long after trimming
     * 
     * @param optionalString
     *            the field that is required
     * @param fieldDescription
     *            the human-readable name of the field
     * @param objectContainingField
     *            the object containing the field being checked
     */
    protected void checkOptionalString(StringWithCustomTags optionalString, String fieldDescription,
            Object objectContainingField) {
        if (optionalString != null && optionalString.value != null && optionalString.value.trim().length() == 0) {
            addError(fieldDescription + " on " + objectContainingField.getClass().getSimpleName()
                    + " is specified, but has a blank value", objectContainingField);
        }
        checkStringWithCustomTags(optionalString, fieldDescription);
    }

    /**
     * Checks that a required string field is specified
     * 
     * @param requiredString
     *            the field that is required
     * @param fieldDescription
     *            the human-readable name of the field
     * @param objectContainingField
     *            the object containing the field being checked
     */
    protected void checkRequiredString(String requiredString, String fieldDescription, Object objectContainingField) {
        if (requiredString == null || requiredString.trim().length() == 0) {
            addError(fieldDescription + " on " + objectContainingField.getClass().getSimpleName()
                    + " is required, but is either null or blank", objectContainingField);
        }
    }

    /**
     * Checks that a required string field is specified
     * 
     * @param requiredString
     *            the field that is required
     * @param fieldDescription
     *            the human-readable name of the field
     * @param objectContainingField
     *            the object containing the field being checked
     */
    protected void checkRequiredString(StringWithCustomTags requiredString, String fieldDescription,
            Object objectContainingField) {
        if (requiredString == null || requiredString.value == null || requiredString.value.trim().length() == 0) {
            addError(fieldDescription + " on " + objectContainingField.getClass().getSimpleName()
                    + " is required, but is either null or blank", objectContainingField);
        }
        checkStringWithCustomTags(requiredString, fieldDescription);
    }

    /**
     * Check a string list (List&lt;String&gt;) on an object. All strings in the list must be non-null and non-blank
     * when trimmed.
     * 
     * @param stringList
     *            the stringlist being validated
     * @param description
     *            a description of the string list
     * @param blanksAllowed
     *            are blank strings allowed in the string list?
     */
    protected void checkStringList(List<String> stringList, String description, boolean blanksAllowed) {
        for (String a : stringList) {
            if (a == null) {
                addError("String list (" + description + ") contains null entry", stringList);
            } else if (!blanksAllowed && a.trim().length() == 0) {
                addError("String list (" + description + ") contains blank entry where none are allowed", stringList);
            }
        }
    }

    /**
     * Check a tagged string list (List&lt;StringWithCustomTags&gt;) on an object. All strings in the list must be
     * non-null and non-blank when trimmed.
     * 
     * @param stringList
     *            the stringlist being validated
     * @param description
     *            a description of the string list
     * @param blanksAllowed
     *            are blank strings allowed in the string list?
     */
    protected void checkStringTagList(List<StringWithCustomTags> stringList, String description, boolean blanksAllowed) {
        for (StringWithCustomTags a : stringList) {
            if (a == null || a.value == null) {
                addError("String list (" + description + ") contains null entry", stringList);
            } else if (!blanksAllowed && a.value.trim().length() == 0) {
                addError("String list (" + description + ") contains blank entry where none are allowed", stringList);
            }
        }
    }

    /**
     * Check a collection of user references
     * 
     * @param userReferences
     *            the collection of user references
     * @param objectWithUserReferences
     *            the object that contains the collection of user references
     */
    protected void checkUserReferences(List<UserReference> userReferences, Object objectWithUserReferences) {
        for (UserReference userReference : userReferences) {
            if (userReference == null) {
                addError("Null user reference in collection on " + objectWithUserReferences.getClass().getSimpleName(),
                        objectWithUserReferences);
            } else {
                checkRequiredString(userReference.referenceNum, "reference number", userReference);
                checkOptionalString(userReference.type, "reference type", userReference);
            }
        }
    }

    /**
     * Check the xref on an object, using the default field name of <tt>xref</tt> for the xref field
     * 
     * @param objectContainingXref
     *            the object containing the xref field
     */
    protected void checkXref(Object objectContainingXref) {
        checkXref(objectContainingXref, "xref");
    }

    /**
     * Check the xref on an object, using a specific field name to find the xref in
     * 
     * @param objectContainingXref
     *            the object containing the xref field
     * @param xrefFieldName
     *            the name of the xref field
     */
    protected void checkXref(Object objectContainingXref, String xrefFieldName) {
        try {
            Field xrefField = objectContainingXref.getClass().getField(xrefFieldName);
            String xref = (String) xrefField.get(objectContainingXref);
            checkRequiredString(xref, xrefFieldName, objectContainingXref);
            if (xref != null) {
                if (xref.length() < 3) {
                    addError("xref on " + objectContainingXref.getClass().getSimpleName()
                            + " is too short to be a valid xref", objectContainingXref);
                } else if (xref.charAt(0) != '@') {
                    addError("xref on " + objectContainingXref.getClass().getSimpleName()
                            + " is doesn't start with an at-sign (@)", objectContainingXref);
                }
                if (!xref.endsWith("@")) {
                    addError("xref on " + objectContainingXref.getClass().getSimpleName()
                            + " is doesn't end with an at-sign (@)", objectContainingXref);
                }
            }
        } catch (SecurityException e) {
            throw new GedcomValidationException(objectContainingXref.getClass().getSimpleName()
                    + " doesn't have an xref to validate", e);
        } catch (ClassCastException e) {
            throw new GedcomValidationException(objectContainingXref.getClass().getSimpleName()
                    + " doesn't have an xref to validate", e);
        } catch (NoSuchFieldException e) {
            throw new GedcomValidationException(objectContainingXref.getClass().getSimpleName()
                    + " doesn't have an xref to validate", e);
        } catch (IllegalArgumentException e) {
            throw new GedcomValidationException(objectContainingXref.getClass().getSimpleName()
                    + " doesn't have an xref to validate", e);
        } catch (IllegalAccessException e) {
            throw new GedcomValidationException(objectContainingXref.getClass().getSimpleName()
                    + " doesn't have an xref to validate", e);
        }
    }

    /**
     * Validate the gedcom file
     */
    protected abstract void validate();

    /**
     * Check a string with custom tags to make sure the custom tags collection is defined whenever there is a value in
     * the string part.
     * 
     * @param swct
     *            the string with custom tags
     * @param fieldDescription
     *            a description of the field's contents
     */
    private void checkStringWithCustomTags(StringWithCustomTags swct, String fieldDescription) {
        if (swct == null) {
            return;
        }
        if (swct.value == null || swct.value.trim().length() == 0) {
            addError("A string with custom tags object (" + fieldDescription + ") was defined with no value", swct);
        }
        checkCustomTags(swct);
    }
}
