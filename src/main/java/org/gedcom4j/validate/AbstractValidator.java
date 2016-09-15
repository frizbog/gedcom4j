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
import java.util.regex.Pattern;

import org.gedcom4j.Options;
import org.gedcom4j.exception.ValidationException;
import org.gedcom4j.model.AbstractAddressableElement;
import org.gedcom4j.model.AbstractCitation;
import org.gedcom4j.model.ChangeDate;
import org.gedcom4j.model.HasCitations;
import org.gedcom4j.model.HasCustomTags;
import org.gedcom4j.model.HasNotes;
import org.gedcom4j.model.HasXref;
import org.gedcom4j.model.ModelElement;
import org.gedcom4j.model.StringTree;
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
     * Regex pattern for an XRef
     */
    private static final Pattern XREF_PATTERN = Pattern.compile("^\\@.+\\@$");

    /**
     * Regex pattern for an Email address
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");

    /**
     * Regex pattern for a URL
     */
    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    /**
     * The root validator - the one that holds the collection of findings among other things.
     */
    protected Validator validator;

    /**
     * Check an object that has an alternate xref field (i.e., contains an xref but isn't named "xref")
     * 
     * @param objectContainingXref
     *            the object containing the xref field
     * @param fieldName
     *            the name of the alternate xref field
     */
    protected void checkAlternateXref(ModelElement objectContainingXref, String fieldName) {
        String xref;
        try {
            xref = (String) get(objectContainingXref, fieldName);
        } catch (ClassCastException e) {
            throw new ValidationException("Field " + fieldName + " on object of type " + objectContainingXref.getClass().getName()
                    + " did not return a string", e);
        }
        if (!isSpecified(xref)) {
            validator.newFinding(objectContainingXref, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, "xref");
        } else {
            if (!XREF_PATTERN.matcher(xref).matches()) {
                validator.newFinding(objectContainingXref, Severity.ERROR, ProblemCode.XREF_INVALID, "xref");
            }
        }
    }

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
        mustHaveValue(changeDate, "date");
        mustHaveValueOrBeOmitted(changeDate, "time");
        if (changeDate.getNotes() == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(changeDate, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "notes");
            initializeCollectionIfAllowed(vf);
        }
        checkNotes(changeDate);
    }

    /**
     * Check citations.
     *
     * @param objectWithCitations
     *            the object with citations
     */
    protected void checkCitations(HasCitations objectWithCitations) {
        List<AbstractCitation> citations = objectWithCitations.getCitations();
        if (citations == null) {
            if (Options.isCollectionInitializationEnabled()) {
                Finding finding = validator.newFinding(objectWithCitations, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION,
                        "citations");
                initializeCollectionIfAllowed(finding);
            }
            return;
        }
        DuplicateHandler<AbstractCitation> dh = new DuplicateHandler<>(citations);
        if (dh.count() > 0) {
            Finding finding = validator.newFinding(objectWithCitations, Severity.ERROR, ProblemCode.DUPLICATE_VALUE, "citations");
            if (validator.mayRepair(finding)) {
                ModelElement before = makeCopy(objectWithCitations);
                dh.remove();
                finding.addRepair(new AutoRepair(before, makeCopy(objectWithCitations)));
            }
        }
        for (AbstractCitation c : citations) {
            new CitationValidator(validator, c).validate();
        }
    }

    /**
     * Check custom tags on an object and initializes the collection of custom tags if needed.
     * 
     * @param o
     *            the object being validated
     */
    protected void checkCustomTags(HasCustomTags o) {
        if (o == null) {
            return; // Nothing to check!
        }
        if (o.getCustomTags() == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(o, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "customTags");
            initializeCollectionIfAllowed(vf);
        }
        if (o.getCustomTags() != null) {
            int i = 0;
            while (i < o.getCustomTags().size()) {
                StringTree ct = o.getCustomTags().get(i);
                if (ct == null) {
                    Finding vf = validator.newFinding(ct, Severity.ERROR, ProblemCode.LIST_WITH_NULL_VALUE, null);
                    if (validator.mayRepair(vf)) {
                        ModelElement before = makeCopy(o);
                        o.getCustomTags().remove(i);
                        vf.addRepair(new AutoRepair(before, makeCopy(o)));
                    } else {
                        i++;
                    }
                } else {
                    checkStringTree(ct);
                    i++;
                }
            }
        }
    }

    /**
     * Check the emails
     * 
     * @param itemWithAddresses
     *            item with emails
     */
    protected void checkEmails(AbstractAddressableElement itemWithAddresses) {
        List<StringWithCustomTags> emails = itemWithAddresses.getEmails();
        if (emails == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(itemWithAddresses, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "emails");
            initializeCollectionIfAllowed(vf);
        }
        if (emails == null) {
            return;
        }
        checkListOfModelElementsForDups(itemWithAddresses, "emails");
        checkListOfModelElementsForDups(itemWithAddresses, "nulls");
        for (StringWithCustomTags swct : emails) {
            mustHaveValue(swct, "value");
            if (swct.getValue() != null && !EMAIL_PATTERN.matcher(swct.getValue()).matches()) {
                validator.newFinding(swct, Severity.WARNING, ProblemCode.NOT_VALID_EMAIL_ADDRESS, "value");
            }
        }
    }

    /**
     * Check the fax numbers
     * 
     * @param itemWithAddresses
     *            item with fax numbers
     */
    protected void checkFaxNumbers(AbstractAddressableElement itemWithAddresses) {
        List<StringWithCustomTags> faxNumbers = itemWithAddresses.getFaxNumbers();
        if (faxNumbers == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(itemWithAddresses, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "faxNumbers");
            initializeCollectionIfAllowed(vf);
        }
        if (faxNumbers == null) {
            return;
        }
        checkListOfModelElementsForDups(itemWithAddresses, "faxNumbers");
        checkListOfModelElementsForDups(itemWithAddresses, "nulls");
        for (StringWithCustomTags swct : faxNumbers) {
            mustHaveValue(swct, "value");
        }
    }

    /**
     * Check a list for null entries
     * 
     * @param modelElement
     *            the object that has the list
     * @param listName
     *            the name of the list
     */
    protected void checkForNullEntries(ModelElement modelElement, String listName) {
        Object object = get(modelElement, listName);
        if (!(object instanceof List)) {
            throw new ValidationException("Could not find a List named " + listName + " on object of type " + modelElement
                    .getClass().getName());
        }
        @SuppressWarnings("rawtypes")
        List list = (List) object;
        int i = 0;
        while (i < list.size()) {
            Object o = list.get(i);
            if (o != null) {
                i++;
                continue;
            }
            Finding vf = validator.newFinding(modelElement, Severity.ERROR, ProblemCode.LIST_WITH_NULL_VALUE, listName);
            if (validator.mayRepair(vf)) {
                list.remove(i);
            } else {
                i++;
            }
        }
    }

    /**
     * Check list of model elements for dups, and removes them if allowed
     *
     * @param <L>
     *            the type of item in the list being checked
     * @param <M>
     *            the type of the object that has the list
     * @param modelElement
     *            the model element that has the list
     * @param listName
     *            the name of the list property
     */
    @SuppressWarnings("checkstyle:NoWhitespaceBefore")
    protected <L extends ModelElement, M extends ModelElement> void checkListOfModelElementsForDups(M modelElement,
            String listName) {
        Object l = get(modelElement, listName);
        if (l == null) {
            return;
        }
        if (!(l instanceof List)) {
            throw new ValidationException("Property " + listName + " on " + modelElement.getClass().getName()
                    + " did not return a List, but a " + l.getClass().getName());
        }
        @SuppressWarnings("unchecked")
        List<L> list = (List<L>) l;
        DuplicateHandler<L> dh = new DuplicateHandler<>(list);
        if (dh.count() > 0) {

            Finding vf = validator.newFinding(modelElement, Severity.ERROR, ProblemCode.DUPLICATE_VALUE, listName);
            if (validator.mayRepair(vf)) {
                @SuppressWarnings("unchecked")
                M before = (M) makeCopy(modelElement);
                dh.remove();
                vf.addRepair(new AutoRepair(before, makeCopy(modelElement)));
            }
        }
    }

    /**
     * Check list of model elements for null values and removes them if allowed.
     *
     * @param <L>
     *            the type of item in the list being checked
     * @param <M>
     *            the type of the object that has the list
     * @param modelElement
     *            the model element that has the list
     * @param listName
     *            the name of the list property
     */
    @SuppressWarnings("checkstyle:NoWhitespaceBefore")
    protected <L extends ModelElement, M extends ModelElement> void checkListOfModelElementsForNulls(M modelElement,
            String listName) {
        Object l = get(modelElement, listName);
        if (l == null) {
            return;
        }
        if (!(l instanceof List)) {
            throw new ValidationException("Property " + listName + " on " + modelElement.getClass().getName()
                    + " did not return a List, but a " + l.getClass().getName());
        }
        @SuppressWarnings("unchecked")
        List<L> list = (List<L>) l;
        NullHandler<L> dh = new NullHandler<>(list);
        if (dh.count() > 0) {

            Finding vf = validator.newFinding(modelElement, Severity.ERROR, ProblemCode.LIST_WITH_NULL_VALUE, listName);
            if (validator.mayRepair(vf)) {
                @SuppressWarnings("unchecked")
                M before = (M) makeCopy(modelElement);
                dh.remove();
                vf.addRepair(new AutoRepair(before, makeCopy(modelElement)));
            }
        }
    }

    /**
     * Check the notes on the object
     * 
     * @param objectWithNotes
     *            the object with notes
     */
    protected void checkNotes(HasNotes objectWithNotes) {
        new NotesListValidator(validator, objectWithNotes).validate();
    }

    /**
     * Check the phone numbers
     * 
     * @param itemWithAddresses
     *            item with phone numbers
     */
    protected void checkPhoneNumbers(AbstractAddressableElement itemWithAddresses) {
        List<StringWithCustomTags> phoneNumbers = itemWithAddresses.getPhoneNumbers();
        if (phoneNumbers == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(itemWithAddresses, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION,
                    "phoneNumbers");
            initializeCollectionIfAllowed(vf);
        }
        if (phoneNumbers == null) {
            return;
        }
        checkListOfModelElementsForDups(itemWithAddresses, "phoneNumbers");
        checkListOfModelElementsForDups(itemWithAddresses, "nulls");
        for (StringWithCustomTags swct : phoneNumbers) {
            mustHaveValue(swct, "value");
        }
    }

    /**
     * Check a string list (List&lt;String&gt; or List&lt;StringWithCustomTags&gt;) on an object.
     * 
     * @param modelElement
     *            the object that has a list of strings in it
     * @param listName
     *            the name of the list of strings to check
     * @param blankStringsAllowed
     *            are blank/empty strings allowed in the string list?
     */
    @SuppressWarnings("unchecked")
    protected void checkStringList(ModelElement modelElement, String listName, boolean blankStringsAllowed) {
        Object o = get(modelElement, listName);
        if (o == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(modelElement, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, listName);
            initializeCollectionIfAllowed(vf);
        }
        o = get(modelElement, listName);
        if (o == null) {
            return;
        }
        if (!(o instanceof List)) {
            throw new ValidationException("Field " + listName + " on object of type " + modelElement.getClass().getName()
                    + " is not a List");
        }
        if (modelElement instanceof HasCustomTags) {
            List<StringWithCustomTags> list = (List<StringWithCustomTags>) o;
            int i = 0;
            while (i < list.size()) {
                StringWithCustomTags swct = list.get(i);
                if (swct != null && (blankStringsAllowed || isSpecified(swct.getValue()))) {
                    checkCustomTags(swct);
                    i++;
                } else {
                    Finding vf = validator.newFinding(modelElement, Severity.ERROR, ProblemCode.LIST_WITH_NULL_VALUE, listName);
                    if (validator.mayRepair(vf)) {
                        ModelElement before = makeCopy(modelElement);
                        list.remove(i);
                        vf.addRepair(new AutoRepair(before, makeCopy(modelElement)));
                    } else {
                        i++;
                    }
                }
            }
        } else {
            if (blankStringsAllowed) {
                return;
            }
            List<String> list = (List<String>) o;
            int i = 0;
            while (i < list.size()) {
                String s = list.get(i);
                if (!isSpecified(s)) {
                    Finding vf = validator.newFinding(modelElement, Severity.ERROR, ProblemCode.LIST_WITH_NULL_VALUE, listName);
                    if (validator.mayRepair(vf)) {
                        ModelElement before = makeCopy(modelElement);
                        list.remove(i);
                        vf.addRepair(new AutoRepair(before, makeCopy(modelElement)));
                    } else {
                        i++;
                    }
                } else {
                    i++;
                }
            }
        }
    }

    /**
     * Check a collection for initialization and fix it, as appropriate
     * 
     * @param objectWithCollection
     *            the object with the collection
     * @param collectionName
     *            the name of the collection
     */
    protected void checkUninitializedCollection(ModelElement objectWithCollection, String collectionName) {
        if (!Options.isCollectionInitializationEnabled()) {
            return;
        }
        if (get(objectWithCollection, collectionName) == null) {
            Finding vf = validator.newFinding(objectWithCollection, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION,
                    collectionName);
            initializeCollectionIfAllowed(vf);
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
        checkUninitializedCollection(objectWithUserReferences, "userReferences");
        if (userReferences == null) {
            return;
        }
        int i = 0;
        while (i < userReferences.size()) {
            UserReference ur = userReferences.get(i);
            if (ur == null) {
                Finding vf = validator.newFinding(objectWithUserReferences, Severity.ERROR, ProblemCode.LIST_WITH_NULL_VALUE,
                        "userReferences");
                if (validator.mayRepair(vf)) {
                    ModelElement before = makeCopy(objectWithUserReferences);
                    userReferences.remove(i);
                    vf.addRepair(new AutoRepair(before, makeCopy(objectWithUserReferences)));
                } else {
                    i++;
                }
            } else {
                mustHaveValue(ur, "referenceNum");
                mustHaveValueOrBeOmitted(ur, "type");
                i++;
            }
        }
    }

    /**
     * Check the wwwUrls
     * 
     * @param itemWithAddresses
     *            item with wwwUrls
     */
    protected void checkWwwUrls(AbstractAddressableElement itemWithAddresses) {
        List<StringWithCustomTags> wwwUrls = itemWithAddresses.getWwwUrls();
        if (wwwUrls == null && Options.isCollectionInitializationEnabled()) {
            Finding vf = validator.newFinding(itemWithAddresses, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "wwwUrls");
            initializeCollectionIfAllowed(vf);
        }
        if (wwwUrls == null) {
            return;
        }
        checkListOfModelElementsForDups(itemWithAddresses, "wwwUrls");
        checkListOfModelElementsForDups(itemWithAddresses, "nulls");
        for (StringWithCustomTags swct : wwwUrls) {
            mustHaveValue(swct, "value");
            if (swct.getValue() != null && !URL_PATTERN.matcher(swct.getValue()).matches()) {
                validator.newFinding(swct, Severity.WARNING, ProblemCode.NOT_VALID_WWW_URL, "value");
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
     * Get the copy constructor on a ModelElement
     * 
     * @param modelElement
     *            the object
     * @return the copy constructor, if one can be found
     */
    @SuppressWarnings("unchecked")
    protected Constructor<ModelElement> getCopyConstructor(ModelElement modelElement) {
        Constructor<ModelElement> result;
        try {
            result = (Constructor<ModelElement>) modelElement.getClass().getConstructor(modelElement.getClass());
        } catch (NoSuchMethodException | SecurityException | IllegalArgumentException e) {
            throw new ValidationException("Unable to find copy constructor on object of class " + modelElement.getClass().getName(),
                    e);
        }
        return result;
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
     * Initialize an uninitialized collection and mark the finding as repaired
     * 
     * @param finding
     *            the finding to be marked as repaired
     */
    @SuppressWarnings("rawtypes")
    protected void initializeCollectionIfAllowed(Finding finding) {
        if (validator.mayRepair(finding)) {
            @SuppressWarnings("PMD.PrematureDeclaration")
            ModelElement before = makeCopy(finding.getItemOfConcern());
            try {
                Field f = finding.getItemOfConcern().getClass().getField(finding.getFieldNameOfConcern());
                f.set(finding.getItemOfConcern(), new ArrayList(0));
            } catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
                throw new ValidationException("Unable to initialize collection '" + finding.getFieldNameOfConcern()
                        + "' on object of class " + finding.getItemOfConcern().getClass().getName(), e);
            }
            ModelElement after = makeCopy(finding.getItemOfConcern());

            finding.addRepair(new AutoRepair(before, after));
        }
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
     * Make a copy of the model element (using reflection)
     * 
     * @param modelElement
     *            the object to copy
     * @return a copy of the object
     */
    protected ModelElement makeCopy(ModelElement modelElement) {
        ModelElement before;
        try {
            Constructor<ModelElement> copyConstructor = getCopyConstructor(modelElement);
            before = copyConstructor.newInstance(modelElement);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ValidationException("Unable to invoke copy constructor on object of class " + modelElement.getClass()
                    .getName(), e);
        }
        return before;
    }

    /**
     * Check that an object in the model has a value for a specific field (by name)
     *
     * @param modelElement
     *            the object that may have a value
     * @param fieldName
     *            the name of the field that may have a value
     */
    protected void mustHaveValue(ModelElement modelElement, String fieldName) {
        Object value = get(modelElement, fieldName);
        if (value == null) {
            validator.newFinding(modelElement, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, fieldName);
            return;
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
        Object value = get(modelElement, fieldName);
        if (value == null) {
            // not there is ok
            return;
        }
        if (value instanceof String) {
            if (!isSpecified((String) value)) {
                validator.newFinding(modelElement, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, fieldName);
            }
        } else if (value instanceof StringWithCustomTags) {
            StringWithCustomTags swct = (StringWithCustomTags) value;
            if (swct.getValue() == null || !isSpecified(swct.getValue())) {
                validator.newFinding(modelElement, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, fieldName);
            }
        } else {
            throw new ValidationException("Don't know how to handle result of type " + value.getClass().getName());
        }
        if (modelElement instanceof HasCustomTags) {
            checkCustomTags((HasCustomTags) modelElement);
        }
    }

    /**
     * Check that an item that should not have a value actually doesn't have one
     * 
     * @param modelElement
     *            the object that to check
     * @param fieldName
     *            the name of the field that should not have a value
     */
    @SuppressWarnings("rawtypes")
    protected void mustNotHaveValue(ModelElement modelElement, String fieldName) {
        Object value = get(modelElement, fieldName);
        if (value == null) {
            return;
        }
        if (value instanceof List && ((List) value).isEmpty()) {
            return;
        }
        validator.newFinding(modelElement, Severity.ERROR, ProblemCode.ILLEGAL_VALUE, fieldName);
    }

    /**
     * Validate the gedcom file
     */
    protected abstract void validate();

    /**
     * Check the xref on an object
     * 
     * @param objectContainingXref
     *            the object containing the xref field
     */
    protected void xrefMustBePresentAndWellFormed(HasXref objectContainingXref) {
        String xref = objectContainingXref.getXref();
        if (!isSpecified(xref)) {
            validator.newFinding(objectContainingXref, Severity.ERROR, ProblemCode.MISSING_REQUIRED_VALUE, "xref");
        } else {
            if (!XREF_PATTERN.matcher(xref).matches()) {
                validator.newFinding(objectContainingXref, Severity.ERROR, ProblemCode.XREF_INVALID, "xref");
            }
        }
    }

    /**
     * Check string tree.
     *
     * @param st
     *            the string tree
     */
    private void checkStringTree(StringTree st) {
        if (st == null) {
            return; // nothing to check
        }
        mustHaveValue(st, "tag");
        mustHaveValueOrBeOmitted(st, "xref");
        mustHaveValue(st, "level");
        if (st.getChildren() != null) {
            for (StringTree ch : st.getChildren()) {
                checkStringTree(ch);
            }
        }
    }

}
