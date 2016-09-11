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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.Options;
import org.gedcom4j.exception.ValidationException;
import org.gedcom4j.model.ChangeDate;
import org.gedcom4j.model.HasCustomTags;
import org.gedcom4j.model.HasXref;
import org.gedcom4j.model.ModelElement;
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.UserReference;
import org.gedcom4j.validate.Validator.Finding;

/**
 * A base class for all validators, to share common validations
 * 
 * @author frizbog1
 * 
 */
@SuppressWarnings({ "PMD.TooManyMethods", "PMD.GodClass" })
abstract class AbstractValidator {

    /**
     * The root validator - the one that holds the collection of findings among other things.
     */
    protected Validator validator;

    /**
     * Check a change date structure
     * 
     * @param changeDate
     *            the change date structure to check
     * @param objectWithChangeDate
     *            the object with the change date
     */
    protected void checkChangeDate(ChangeDate changeDate, ModelElement objectWithChangeDate) {
        if (changeDate == null) {
            // Change dates are always optional
            return;
        }
        checkRequiredString(changeDate.getDate(), "change date", objectWithChangeDate);
        checkOptionalString(changeDate.getTime(), "change time", objectWithChangeDate);
        if (changeDate.getNotes() == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(objectWithChangeDate, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "notes");
            if (validator.mayRepair(vf)) {
                ChangeDate before = new ChangeDate(changeDate);
                changeDate.getNotes(true).clear();
                vf.addRepair(new AutoRepair(before, new ChangeDate(changeDate)));
            }
        } else {
            new NotesValidator(validator, changeDate, changeDate.getNotes()).validate();
        }

    }

    /**
     * Check custom tags on an object. Uses reflection to look for a getter named "getCustomTags", invokes it, and checks the
     * result. If autorepair is on, it will reflectively fix this.
     * 
     * @param o
     *            the object being validated
     */
    protected void checkCustomTags(HasCustomTags o) {

        Method customTagsGetter = null;
        try {
            customTagsGetter = o.getClass().getMethod("getCustomTags");
        } catch (@SuppressWarnings("unused") SecurityException unusedAndIgnored) {
            addError("Cannot access getter named 'getCustomTags' on object of type " + o.getClass().getSimpleName() + ".", o);
            return;
        } catch (@SuppressWarnings("unused") NoSuchMethodException unusedAndIgnored) {
            addError("Cannot find getter named 'getCustomTags' on object of type " + o.getClass().getSimpleName() + ".", o);
            return;
        }

        Object fldVal = null;
        try {
            fldVal = customTagsGetter.invoke(o);
        } catch (IllegalArgumentException e) {
            addError("Cannot get value of customTags attribute on object of type " + o.getClass().getSimpleName() + " - " + e
                    .getMessage(), o);
            return;
        } catch (IllegalAccessException e) {
            addError("Cannot get value of customTags attribute on object of type " + o.getClass().getSimpleName() + " - " + e
                    .getMessage(), o);
            return;
        } catch (InvocationTargetException e) {
            addError("Cannot get value of customTags attribute on object of type " + o.getClass().getSimpleName() + " - " + e
                    .getMessage(), o);
            return;
        }
        if (fldVal == null && Options.isCollectionInitializationEnabled()) {
            if (validator.isAutorepairEnabled()) {
                try {
                    customTagsGetter.invoke(o, true);
                } catch (IllegalArgumentException e) {
                    addError("Cannot autorepair value of customTags attribute on object of type " + o.getClass().getSimpleName()
                            + " - " + e.getMessage(), o);
                    return;
                } catch (IllegalAccessException e) {
                    addError("Cannot autorepair value of customTags attribute on object of type " + o.getClass().getSimpleName()
                            + " - " + e.getMessage(), o);
                    return;
                } catch (InvocationTargetException e) {
                    addError("Cannot autorepair value of customTags attribute on object of type " + o.getClass().getSimpleName()
                            + " - " + e.getMessage(), o);
                    return;
                }
                validator.addInfo("Custom tag collection was null - repaired", o);
            } else {
                validator.addError("Custom tag collection is null - must be at least an empty collection", o);
            }
        } else {
            if (fldVal != null && !(fldVal instanceof List)) {
                validator.addError("Custom tag collection is not a List", o);
            }
        }
    }

    /**
     * Check a string list (List&lt;String&gt;) on an object. All strings in the list must be non-null and non-blank when trimmed.
     * 
     * @param stringList
     *            the stringlist being validated
     * @param description
     *            a description of the string list
     * @param blanksAllowed
     *            are blank strings allowed in the string list?
     */
    protected void checkStringList(List<String> stringList, String description, boolean blanksAllowed) {
        int i = 0;
        if (stringList != null) {
            while (i < stringList.size()) {
                String a = stringList.get(i);
                if (a == null) {
                    if (validator.isAutorepairEnabled()) {
                        addInfo("String list (" + description + ") contains null entry - removed", stringList);
                        stringList.remove(i);
                        continue;
                    }
                    addError("String list (" + description + ") contains null entry", stringList);
                } else if (!blanksAllowed && !isSpecified(a)) {
                    if (validator.isAutorepairEnabled()) {
                        addInfo("String list (" + description + ") contains blank entry where none are allowed - removed",
                                stringList);
                        stringList.remove(i);
                        continue;
                    }
                    addError("String list (" + description + ") contains blank entry where none are allowed", stringList);
                }
                i++;
            }
        }
    }

    /**
     * Check a tagged string list (List&lt;StringWithCustomTags&gt;) on an object. All strings in the list must be non-null and
     * non-blank when trimmed.
     * 
     * @param stringList
     *            the stringlist being validated
     * @param description
     *            a description of the string list
     * @param blanksAllowed
     *            are blank strings allowed in the string list?
     */
    protected void checkStringTagList(List<StringWithCustomTags> stringList, String description, boolean blanksAllowed) {
        int i = 0;
        if (validator.isAutorepairEnabled()) {
            int dups = new DuplicateEliminator<>(stringList).process();
            if (dups > 0) {
                validator.addInfo(dups + " duplicate tagged strings found and removed", stringList);
            }
        }

        if (stringList != null) {
            while (i < stringList.size()) {
                StringWithCustomTags a = stringList.get(i);
                if (a == null || a.getValue() == null) {
                    if (validator.isAutorepairEnabled()) {
                        addInfo("String list (" + description + ") contains null entry - removed", stringList);
                        stringList.remove(i);
                        continue;
                    }
                    addError("String list (" + description + ") contains null entry", stringList);
                } else if (!blanksAllowed && a.getValue().trim().length() == 0) {
                    if (validator.isAutorepairEnabled()) {
                        addInfo("String list (" + description + ") contains blank entry where none are allowed - removed",
                                stringList);
                        stringList.remove(i);
                        continue;
                    }
                    addError("String list (" + description + ") contains blank entry where none are allowed", stringList);
                }
                i++;
            }
        }
    }

    protected void checkUninitializedCollection(ModelElement objectWithCollection, String collectionName) {
        if (!Options.isCollectionInitializationEnabled()) {
            return;
        }
        if (get(objectWithCollection, collectionName) == null) {
            Finding vf = validator.newFinding(objectWithCollection, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION,
                    collectionName);
            initializeCollectionIfAllowed(vf, objectWithCollection, collectionName);
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
    protected void checkUserReferences(List<UserReference> userReferences, ModelElement objectWithUserReferences) {
        if (userReferences == null) {
            // finding, initialize
        } else {
            for (UserReference userReference : userReferences) {
                if (userReference == null) {
                    addError("Null user reference in collection on " + objectWithUserReferences.getClass().getSimpleName(),
                            objectWithUserReferences);
                } else {
                    checkRequiredString(userReference.getReferenceNum(), "reference number", userReference);
                    checkOptionalString(userReference.getType(), "reference type", userReference);
                }
            }
        }
    }

    /**
     * Check the xref on an object, using a specific field name to find the xref in
     * 
     * @param objectContainingXref
     *            the object containing the xref field
     * @param xrefFieldName
     *            the name of the xref field
     */
    protected void checkXref(HasXref objectContainingXref) {
        String xref = objectContainingXref.getXref();
        if (!isSpecified(xref)) {
            // TODO - log finding
        } else {
            if (xref.length() < 3) {
                // TODO log finding
                // addError("xref on " + objectContainingXref.getClass().getSimpleName() + " is too short to be a valid xref",
                // objectContainingXref);
            } else if (xref.charAt(0) != '@') {
                // TODO log finding
                // addError("xref on " + objectContainingXref.getClass().getSimpleName() + " is doesn't start with an at-sign (@)",
                // objectContainingXref);
            }
            if (!xref.endsWith("@")) {
                // TODO log finding
                // addError("xref on " + objectContainingXref.getClass().getSimpleName() + " is doesn't end with an at-sign (@)",
                // objectContainingXref);
            }
        }
    }

    /**
     * Get the value for a field whose name is supplied for a given object
     * 
     * @param modelElement
     *            the object that has the named field you want a getter for
     * @param fieldName
     *            the name of the field you want to get
     * @return the value of the named field
     */
    protected Object get(ModelElement modelElement, String fieldName) {
        Method getter = getGetter(modelElement, fieldName);
        try {
            return getter.invoke(modelElement);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ValidationException("Unable to invoke getter method for field '" + fieldName + "' on object of type "
                    + modelElement.getClass().getName(), e);
        }
    }

    /**
     * Get the getter for a field whose name is supplied for a given object
     * 
     * @param modelElement
     *            the object that has the named field you want a getter for
     * @param fieldName
     *            the name of the field you want to get
     * @return the getter method
     */
    @SuppressWarnings("PMD.PreserveStackTrace")
    protected Method getGetter(ModelElement modelElement, String fieldName) {
        Method result = null;
        try {
            String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            result = modelElement.getClass().getMethod(getterName);
        } catch (@SuppressWarnings("unused") NoSuchMethodException | SecurityException ignored) {
            try {
                String getterName = "is" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                result = modelElement.getClass().getMethod(getterName);
            } catch (NoSuchMethodException | SecurityException e1) {
                throw new ValidationException("Unable to find getter method for field '" + fieldName + "' on object of type "
                        + modelElement.getClass().getName(), e1);
            }
        }
        return result;
    }

    /**
     * Is the string supplied non-null, and has something other than whitespace in it?
     * 
     * @param s
     *            the strings
     * @return true if the string supplied non-null, and has something other than whitespace in it
     */
    protected boolean isSpecified(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check that a string with custom tags either has a non-blank/non-null value or be omitted.
     *
     * @param modelElement
     *            the object that may have a value
     * @param fieldName
     *            the name of the field that may have a value
     */
    protected void mustHaveValue(ModelElement modelElement, String fieldName) {
        try {
            Method m = modelElement.getClass().getMethod("get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(
                    1));

            StringWithCustomTags swct = (StringWithCustomTags) m.invoke(modelElement);
            if (swct == null) {
            }
            if (swct.getValue() != null && !isSpecified(swct.getValue())) {
                validator.newFinding(modelElement, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, null);
            }
        } catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new ValidationException("Unable to reflectively get StringWithCustomTags named " + fieldName + " from "
                    + modelElement, e);
        }

        if (modelElement instanceof HasCustomTags) {
            checkCustomTags((HasCustomTags) modelElement);
        }

    }

    /**
     * Check that a string or string with custom tags is either null, or has a non-blank/non-null value
     *
     * @param modelElement
     *            the object that may have a value
     * @param fieldName
     *            the name of the field that may have a value
     */
    protected void mustHaveValueOrBeOmitted(ModelElement modelElement, String fieldName) {
        try {
            Method m = modelElement.getClass().getMethod("get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(
                    1));
            Object value = m.invoke(modelElement);
            if (value == null) {
                return; // It was omitted, so it's ok
            }
            if (value instanceof String) {
                if (!isSpecified((String) value)) {
                    validator.newFinding(modelElement, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, fieldName);
                }
            } else if (value instanceof StringWithCustomTags) {
                StringWithCustomTags swct = (StringWithCustomTags) value;
                if (swct.getValue() != null && !isSpecified(swct.getValue())) {
                    validator.newFinding(modelElement, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, fieldName);
                }
            } else {
                throw new ValidationException("Don't know how to handle result of type " + value.getClass().getName());
            }
        } catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new ValidationException("Unable to reflectively get StringWithCustomTags named " + fieldName + " from "
                    + modelElement, e);
        }

        if (modelElement instanceof HasCustomTags) {
            checkCustomTags((HasCustomTags) modelElement);
        }

    }

    /**
     * Validate the gedcom file
     */
    protected abstract void validate();

    /**
     * Initialize an uninitialized collection and mark the finding as repaired
     * 
     * @param finding
     *            the finding to be marked as repaired
     * @param objectWithCollection
     *            the object that has the uninitialized collection
     * @param collectionName
     *            the name of the collection
     */
    @SuppressWarnings("rawtypes")
    private void initializeCollectionIfAllowed(Finding finding, ModelElement objectWithCollection, String collectionName) {
        if (validator.mayRepair(finding)) {
            ModelElement before;
            Constructor copyConstructor;
            try {
                copyConstructor = objectWithCollection.getClass().getConstructor(objectWithCollection.getClass());
                before = (ModelElement) copyConstructor.newInstance(objectWithCollection);
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException e1) {
                throw new ValidationException("Unable to find copy constructor on object of class " + objectWithCollection
                        .getClass().getName(), e1);
            }
            try {
                Field f = objectWithCollection.getClass().getField(collectionName);
                f.set(objectWithCollection, new ArrayList(0));
            } catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
                throw new ValidationException("Unable to initialize collection '" + collectionName + "' on object of class "
                        + objectWithCollection.getClass().getName(), e);
            }
            ModelElement after;
            try {
                after = (ModelElement) copyConstructor.newInstance(objectWithCollection);
            } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e1) {
                throw new ValidationException("Unable to find copy constructor on object of class " + objectWithCollection
                        .getClass().getName(), e1);
            }

            finding.addRepair(new AutoRepair(before, after));
        }
    }

}
