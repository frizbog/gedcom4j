package com.mattharrah.gedcom4j.validate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.mattharrah.gedcom4j.ChangeDate;
import com.mattharrah.gedcom4j.Note;
import com.mattharrah.gedcom4j.UserReference;

/**
 * A base class for all validators
 * 
 * @author frizbog1
 * 
 */
public abstract class AbstractValidator {

    /**
     * The root validator - the one that holds the collection of findings among
     * other things
     */
    protected GedcomValidator rootValidator;

    /**
     * Add a new finding of severity ERROR
     * 
     * @param description
     *            the description of the error
     */
    protected void addError(String description) {
        rootValidator.findings.add(new GedcomValidationFinding(description,
                Severity.ERROR, null));
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
        rootValidator.findings.add(new GedcomValidationFinding(description,
                Severity.ERROR, o));
    }

    /**
     * Add a new finding of severity INFO
     * 
     * @param description
     *            the description of the finding
     */
    protected void addInfo(String description) {
        rootValidator.findings.add(new GedcomValidationFinding(description,
                Severity.INFO, null));
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
        rootValidator.findings.add(new GedcomValidationFinding(description,
                Severity.INFO, o));
    }

    /**
     * Add a new finding of severity WARNING
     * 
     * @param description
     *            the description of the warning
     */
    protected void addWarning(String description) {
        rootValidator.findings.add(new GedcomValidationFinding(description,
                Severity.WARNING, null));
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
        rootValidator.findings.add(new GedcomValidationFinding(description,
                Severity.WARNING, o));
    }

    /**
     * Check a change date structure
     * 
     * @param changeDate
     *            the change date structure to check
     * @param objectWithChangeDate
     *            the object with the change date
     */
    protected void checkChangeDate(ChangeDate changeDate,
            Object objectWithChangeDate) {
        if (changeDate == null) {
            // Change dates are always optional
            return;
        }
        checkRequiredString(changeDate.date, "change date",
                objectWithChangeDate);
        checkOptionalString(changeDate.time, "change time",
                objectWithChangeDate);
        if (changeDate.notes == null) {
            if (rootValidator.autorepair) {
                changeDate.notes = new ArrayList<Note>();
                addInfo("Notes collection was null on "
                        + changeDate.getClass().getSimpleName()
                        + " - autorepaired");
            } else {
                addError("Notes collection is null on "
                        + changeDate.getClass().getSimpleName());
            }
        } else {
            checkNotes(changeDate.notes, changeDate);
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
     * Checks that an optional string field is either null, or greater than zero
     * characters long after trimming
     * 
     * @param optionalString
     *            the field that is required
     * @param fieldDescription
     *            the human-readable name of the field
     * @param objectContainingField
     *            the object containing the field being checked
     */
    protected void checkOptionalString(String optionalString,
            String fieldDescription, Object objectContainingField) {
        if (optionalString != null && optionalString.trim().length() == 0) {
            addError(fieldDescription + " on "
                    + objectContainingField.getClass().getSimpleName()
                    + " is specified, but has a blank value",
                    objectContainingField);
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
    protected void checkRequiredString(String requiredString,
            String fieldDescription, Object objectContainingField) {
        if (requiredString == null || requiredString.trim().length() == 0) {
            addError(fieldDescription + " on "
                    + objectContainingField.getClass().getSimpleName()
                    + " is required, but is either null or blank",
                    objectContainingField);
        }
    }

    /**
     * Check a string list (List&lt;String&gt;) on an object. All strings in the
     * list must be non-null and non-blank when trimmed.
     * 
     * @param stringList
     *            the stringlist being validated
     * @param description
     *            a description of the string list
     * @param blanksAllowed
     *            are blank strings allowed in the string list?
     */
    protected void checkStringList(List<String> stringList, String description,
            boolean blanksAllowed) {
        for (String a : stringList) {
            if (a == null) {
                addError("String list (" + description
                        + ") contains null entry", stringList);
            } else if (!blanksAllowed && a.trim().length() == 0) {
                addError("String list (" + description
                        + ") contains blank entry where none are allowed",
                        stringList);
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
    protected void checkUserReferences(List<UserReference> userReferences,
            Object objectWithUserReferences) {
        for (UserReference userReference : userReferences) {
            if (userReference == null) {
                addError("Null user reference in collection on "
                        + objectWithUserReferences.getClass().getSimpleName(),
                        objectWithUserReferences);
            } else {
                checkRequiredString(userReference.referenceNum,
                        "reference number", userReference);
                checkOptionalString(userReference.type, "reference type",
                        userReference);
            }
        }
    }

    /**
     * Check the xref on an object, using the default field name of
     * <tt>xref</tt> for the xref field
     * 
     * @param objectContainingXref
     *            the object containing the xref field
     */
    protected void checkXref(Object objectContainingXref) {
        checkXref(objectContainingXref, "xref");
    }

    /**
     * Check the xref on an object, using a specific field name to find the xref
     * in
     * 
     * @param objectContainingXref
     *            the object containing the xref field
     * @param xrefFieldName
     *            the name of the xref field
     */
    protected void checkXref(Object objectContainingXref, String xrefFieldName) {
        try {
            Field xrefField = objectContainingXref.getClass().getField(
                    xrefFieldName);
            String xref = (String) xrefField.get(objectContainingXref);
            checkRequiredString(xref, xrefFieldName, objectContainingXref);
            if (xref != null) {
                if (xref.length() < 3) {
                    addError("xref on "
                            + objectContainingXref.getClass().getSimpleName()
                            + " is too short to be a valid xref",
                            objectContainingXref);
                }
                if (!xref.startsWith("@")) {
                    addError("xref on "
                            + objectContainingXref.getClass().getSimpleName()
                            + " is doesn't start with an at-sign (@)",
                            objectContainingXref);
                }
                if (!xref.endsWith("@")) {
                    addError("xref on "
                            + objectContainingXref.getClass().getSimpleName()
                            + " is doesn't end with an at-sign (@)",
                            objectContainingXref);
                }
            }
        } catch (SecurityException e) {
            throw new GedcomValidationException(objectContainingXref.getClass()
                    .getSimpleName() + " doesn't have an xref to validate", e);
        } catch (ClassCastException e) {
            throw new GedcomValidationException(objectContainingXref.getClass()
                    .getSimpleName() + " doesn't have an xref to validate", e);
        } catch (NoSuchFieldException e) {
            throw new GedcomValidationException(objectContainingXref.getClass()
                    .getSimpleName() + " doesn't have an xref to validate", e);
        } catch (IllegalArgumentException e) {
            throw new GedcomValidationException(objectContainingXref.getClass()
                    .getSimpleName() + " doesn't have an xref to validate", e);
        } catch (IllegalAccessException e) {
            throw new GedcomValidationException(objectContainingXref.getClass()
                    .getSimpleName() + " doesn't have an xref to validate", e);
        }
    }

    /**
     * Are there any errors in the findings (so far)?
     * 
     * @return true if there exists at least one finding with severity ERROR
     */
    protected boolean hasErrors() {
        for (GedcomValidationFinding finding : rootValidator.findings) {
            if (finding.severity == Severity.ERROR) {
                return true;
            }
        }
        return false;
    }

    /**
     * Are there any warnings in the findings (so far)?
     * 
     * @return true if there exists at least one finding with severity WARNING
     */
    protected boolean hasWarnings() {
        for (GedcomValidationFinding finding : rootValidator.findings) {
            if (finding.severity == Severity.WARNING) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validate the gedcom file
     */
    protected abstract void validate();
}
