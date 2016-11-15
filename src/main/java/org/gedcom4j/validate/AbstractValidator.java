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

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.gedcom4j.Options;
import org.gedcom4j.exception.ValidationException;
import org.gedcom4j.model.AbstractAddressableElement;
import org.gedcom4j.model.AbstractCitation;
import org.gedcom4j.model.ChangeDate;
import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.HasCitations;
import org.gedcom4j.model.HasCustomFacts;
import org.gedcom4j.model.HasNotes;
import org.gedcom4j.model.HasXref;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.ModelElement;
import org.gedcom4j.model.MultiStringWithCustomFacts;
import org.gedcom4j.model.StringWithCustomFacts;
import org.gedcom4j.model.UserReference;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.parser.DateParser;
import org.gedcom4j.parser.DateParser.ImpreciseDatePreference;
import org.gedcom4j.validate.Validator.Finding;

/**
 * A base class for all validators, to share common validations
 * 
 * @author frizbog1
 * 
 */
@SuppressWarnings({ "PMD.TooManyMethods", "PMD.GodClass", "PMD.ExcessiveImports" })
abstract class AbstractValidator implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -4649410344505048925L;

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
    private final Validator validator;

    /**
     * Instantiates a new abstract validator.
     *
     * @param v
     *            the root {@link Validator} that tracks findings, etc.
     */
    AbstractValidator(Validator v) {
        validator = v;
    }

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
            validator.newFinding(objectContainingXref, Severity.ERROR, org.gedcom4j.validate.ProblemCode.MISSING_REQUIRED_VALUE,
                    "xref");
        } else {
            if (!XREF_PATTERN.matcher(xref).matches()) {
                validator.newFinding(objectContainingXref, Severity.ERROR, org.gedcom4j.validate.ProblemCode.XREF_INVALID, "xref");
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
        mustBeDateIfSpecified(changeDate, "date");
        mustHaveValueOrBeOmitted(changeDate, "time");
        if (objectWithChangeDate instanceof HasNotes) {
            checkUninitializedCollection(changeDate, "noteStructures");
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
        checkUninitializedCollection(objectWithCitations, "citations");
        List<AbstractCitation> citations = objectWithCitations.getCitations();
        if (citations == null) {
            return;
        }
        DuplicateHandler<AbstractCitation> dh = new DuplicateHandler<>(citations);
        if (dh.count() > 0) {
            Finding finding = validator.newFinding(objectWithCitations, Severity.ERROR,
                    org.gedcom4j.validate.ProblemCode.DUPLICATE_VALUE, "citations");
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
     * Check custom facts on an object and initializes the collection of custom facts if needed.
     * 
     * @param o
     *            the object being validated
     */
    protected void checkCustomFacts(HasCustomFacts o) {
        if (o == null) {
            return; // Nothing to check!
        }
        checkUninitializedCollection(o, "customFacts");
        if (o.getCustomFacts() == null) {
            return;
        }
        int i = 0;
        while (i < o.getCustomFacts().size()) {
            CustomFact cf = o.getCustomFacts().get(i);
            if (cf == null) {
                Finding vf = validator.newFinding(o, Severity.ERROR, org.gedcom4j.validate.ProblemCode.LIST_WITH_NULL_VALUE,
                        "customFacts");
                if (validator.mayRepair(vf)) {
                    ModelElement before = makeCopy(o);
                    o.getCustomFacts().remove(i);
                    vf.addRepair(new AutoRepair(before, makeCopy(o)));
                } else {
                    i++;
                }
            } else {
                mustBeDateIfSpecified(cf, "date");
                if (cf.getPlace() != null) {
                    new PlaceValidator(getValidator(), cf.getPlace()).validate();
                }
                checkNotes(cf);
                checkCitations(cf);
                checkCustomFacts(cf);
                i++;
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
        checkUninitializedCollection(itemWithAddresses, "emails");
        if (itemWithAddresses.getEmails() == null) {
            return;
        }
        checkListOfModelElementsForDups(itemWithAddresses, "emails");
        checkListOfModelElementsForNulls(itemWithAddresses, "emails");
        for (StringWithCustomFacts swct : itemWithAddresses.getEmails()) {
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
        checkUninitializedCollection(itemWithAddresses, "faxNumbers");
        if (itemWithAddresses.getFaxNumbers() == null) {
            return;
        }
        checkListOfModelElementsForDups(itemWithAddresses, "faxNumbers");
        checkListOfModelElementsForNulls(itemWithAddresses, "faxNumbers");
        for (StringWithCustomFacts swct : itemWithAddresses.getFaxNumbers()) {
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
            Finding vf = validator.newFinding(modelElement, Severity.ERROR, org.gedcom4j.validate.ProblemCode.LIST_WITH_NULL_VALUE,
                    listName);
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
            Finding vf = validator.newFinding(modelElement, Severity.ERROR, org.gedcom4j.validate.ProblemCode.DUPLICATE_VALUE,
                    listName);
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

            Finding vf = validator.newFinding(modelElement, Severity.ERROR, org.gedcom4j.validate.ProblemCode.LIST_WITH_NULL_VALUE,
                    listName);
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
        new NoteStructureListValidator(validator, objectWithNotes).validate();
    }

    /**
     * Check the phone numbers
     * 
     * @param itemWithAddresses
     *            item with phone numbers
     */
    protected void checkPhoneNumbers(AbstractAddressableElement itemWithAddresses) {
        checkUninitializedCollection(itemWithAddresses, "phoneNumbers");
        if (itemWithAddresses.getPhoneNumbers() == null) {
            return;
        }
        checkListOfModelElementsForDups(itemWithAddresses, "phoneNumbers");
        checkListOfModelElementsForNulls(itemWithAddresses, "phoneNumbers");
        for (StringWithCustomFacts swct : itemWithAddresses.getPhoneNumbers()) {
            mustHaveValue(swct, "value");
        }
    }

    /**
     * Check a string list (List&lt;String&gt; or List&lt;StringWithCustomFacts&gt;) on an object.
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
        checkUninitializedCollection(modelElement, listName);
        Object o = get(modelElement, listName);
        if (o == null) {
            return;
        }
        if (!(o instanceof List) && !(o instanceof MultiStringWithCustomFacts)) {
            throw new ValidationException("Field " + listName + " on object of type " + modelElement.getClass().getName()
                    + " is not a List");
        }
        if (o instanceof MultiStringWithCustomFacts) {
            MultiStringWithCustomFacts ms = (MultiStringWithCustomFacts) o;
            checkCustomFacts(ms);
        } else if (modelElement instanceof HasCustomFacts) {
            Iterator<?> itr = ((List<?>) o).iterator();
            while (itr.hasNext()) {
                Object s = itr.next();
                if (s instanceof StringWithCustomFacts) {
                    StringWithCustomFacts swct = (StringWithCustomFacts) s;
                    if (blankStringsAllowed || isSpecified(swct.getValue())) {
                        checkCustomFacts(swct);
                        continue;
                    }
                    Finding vf = validator.newFinding(modelElement, Severity.ERROR,
                            org.gedcom4j.validate.ProblemCode.LIST_WITH_NULL_VALUE, listName);
                    if (validator.mayRepair(vf)) {
                        ModelElement before = makeCopy(modelElement);
                        itr.remove();
                        vf.addRepair(new AutoRepair(before, makeCopy(modelElement)));
                    }
                } else if (s instanceof String) {
                    String st = (String) s;
                    if (blankStringsAllowed || isSpecified(st)) {
                        continue;
                    }
                    Finding vf = validator.newFinding(modelElement, Severity.ERROR,
                            org.gedcom4j.validate.ProblemCode.LIST_WITH_NULL_VALUE, listName);
                    if (validator.mayRepair(vf)) {
                        ModelElement before = makeCopy(modelElement);
                        itr.remove();
                        vf.addRepair(new AutoRepair(before, makeCopy(modelElement)));
                    }
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
            Finding vf = validator.newFinding(objectWithCollection, Severity.INFO,
                    org.gedcom4j.validate.ProblemCode.UNINITIALIZED_COLLECTION, collectionName);
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
                Finding vf = validator.newFinding(objectWithUserReferences, Severity.ERROR,
                        org.gedcom4j.validate.ProblemCode.LIST_WITH_NULL_VALUE, "userReferences");
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
        checkUninitializedCollection(itemWithAddresses, "wwwUrls");
        if (itemWithAddresses.getWwwUrls() == null) {
            return;
        }
        checkListOfModelElementsForDups(itemWithAddresses, "wwwUrls");
        checkListOfModelElementsForNulls(itemWithAddresses, "wwwUrls");
        for (StringWithCustomFacts swct : itemWithAddresses.getWwwUrls()) {
            mustHaveValue(swct, "value");
            if (swct.getValue() != null && !URL_PATTERN.matcher(swct.getValue()).matches()) {
                validator.newFinding(swct, Severity.WARNING, org.gedcom4j.validate.ProblemCode.NOT_VALID_WWW_URL, "value");
            }
        }
    }

    /**
     * Get the value for a field whose name is supplied for a given object
     * 
     * @param object
     *            the object that has the named field you want a getter for
     * @param fieldName
     *            the name of the field you want to get
     * @return the value of the named field
     */
    protected Object get(Object object, String fieldName) {
        Method getter = getGetter(object, fieldName);
        try {
            return getter.invoke(object);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ValidationException("Unable to invoke getter method for field '" + fieldName + "' on object of type " + object
                    .getClass().getName(), e);
        }
    }

    /**
     * Gets the earliest event of a given type on an individual
     *
     * @param i
     *            the individual
     * @param type
     *            the type of event to find
     * @return the earliest event of type
     */
    protected IndividualEvent getEarliestEventOfType(Individual i, IndividualEventType type) {
        if (i == null) {
            return null;
        }
        IndividualEvent result = null;
        List<IndividualEvent> eventsOfType = i.getEventsOfType(type);
        DateParser dp = new DateParser();
        Date earliestSoFar = new Date(Long.MAX_VALUE);
        for (IndividualEvent e : eventsOfType) {
            if (e.getDate() != null && e.getDate().getValue() != null) {
                Date d = dp.parse(e.getDate().getValue(), ImpreciseDatePreference.FAVOR_EARLIEST);
                if (d != null && d.before(earliestSoFar)) {
                    result = e;
                    earliestSoFar = d;
                }
            }
        }
        return result;
    }

    /**
     * Gets the latest event of a given type on an individual
     *
     * @param i
     *            the individual
     * @param type
     *            the type of event to find
     * @return the latest event of type
     */
    protected IndividualEvent getLatestEventOfType(Individual i, IndividualEventType type) {
        if (i == null) {
            return null;
        }
        IndividualEvent result = null;
        List<IndividualEvent> eventsOfType = i.getEventsOfType(type);
        DateParser dp = new DateParser();
        Date latestSoFar = new Date(Long.MIN_VALUE);
        for (IndividualEvent e : eventsOfType) {
            if (e.getDate() != null && e.getDate().getValue() != null) {
                Date d = dp.parse(e.getDate().getValue(), ImpreciseDatePreference.FAVOR_LATEST);
                if (d != null && d.after(latestSoFar)) {
                    result = e;
                    latestSoFar = d;
                }
            }
        }
        return result;
    }

    /**
     * Get the validator
     * 
     * @return the validator
     */
    protected Validator getValidator() {
        return validator;
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
     * Check if the finding can be auto-repaired. Delegates to the registered auto-repair responder, if any.
     * 
     * @param validationFinding
     *            the validation finding
     * @return true if the finding may be auto-repaired
     */
    protected boolean mayRepair(Finding validationFinding) {
        return validator.mayRepair(validationFinding);
    }

    /**
     * Check that object is a well-formed date string in GEDCOM format, or null/empty.
     *
     * @param modelElement
     *            the model element with a date field
     * @param dateFieldName
     *            the name date field
     */
    protected void mustBeDateIfSpecified(ModelElement modelElement, String dateFieldName) {
        Object object = get(modelElement, dateFieldName);
        if (object == null) {
            return;
        }
        String dateToValidate = null;
        if (object instanceof String) {
            dateToValidate = (String) object;
        } else if (object instanceof StringWithCustomFacts) {
            dateToValidate = ((StringWithCustomFacts) object).getValue();
        }
        if (!isSpecified(dateToValidate)) {
            return;
        }
        DateParser dp = new DateParser();
        if (dp.parse(dateToValidate) == null) {
            validator.newFinding(modelElement, Severity.ERROR, ProblemCode.INVALID_DATE, dateFieldName);
        }
    }

    /**
     * <p>
     * Validate that a named field on the supplied object has a value that can be found in a supplied enum, or isn't there (it's
     * empty or null).
     * </p>
     * <p>
     * The check is made through a series of attempts:
     * </p>
     * <ol>
     * <li>See if the value of the field equals any of the enumerated constants in the enum</li>
     * <li>If the value of the field is a string, see if it matches any of the following:
     * <ol>
     * <li>The name (as a string) of the enum constant</li>
     * <li>The value of a property in the enum constant named "code" (if a getCode() getter exists)</li>
     * </ol>
     * </ol>
     * 
     * @param <E>
     *            foo
     * @param e
     *            the enum that the value must be in
     * @param modelElement
     *            the object that has the field to be checked
     * @param fieldName
     *            the name of the field with the value that needs to be in the enum
     */
    @SuppressWarnings({ "checkstyle:NoWhitespaceBefore", "PMD.EmptyCatchBlock" })
    protected <E extends Enum<E>> void mustBeInEnumIfSpecified(Class<E> e, ModelElement modelElement, String fieldName) {
        if (!e.isEnum()) {
            throw new ValidationException("Class of type " + e.getClass().getName() + " is not an enum");
        }
        Object object = get(modelElement, fieldName);
        if (object == null) {
            // No value to check, so no problem
            return;
        }

        for (Enum<E> c : e.getEnumConstants()) {
            if (c.equals(object)) {
                // The field had a value that was equal to one of the enum constants, so it's ok
                return;
            }
        }

        // Get a string value from the field value - we're going to see if there's a code
        String val = null;
        if (object instanceof StringWithCustomFacts) {
            val = ((StringWithCustomFacts) object).getValue();
        } else if (object instanceof String) {
            val = (String) object;
        }
        if (val == null) {
            // No value to check, so no problem
            return;
        }
        for (Enum<E> c : e.getEnumConstants()) {
            if (c.name().equals(val)) {
                // We matched an enum constant's name, so it's ok
                return;
            }
            try {
                Object enumCode = get(c, "code");
                if (val.equals(enumCode)) {
                    return;
                }
            } catch (@SuppressWarnings("unused") ValidationException ignored) {
                // No getCode() getter on the enum constant
            }
        }
        // IF we've fallen through to here, we failed to find a match in the enum and need to register a finding
        validator.newFinding(modelElement, Severity.ERROR, ProblemCode.ILLEGAL_VALUE, fieldName);
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
            validator.newFinding(modelElement, Severity.ERROR, org.gedcom4j.validate.ProblemCode.MISSING_REQUIRED_VALUE, fieldName);
            return;
        }
        if (value instanceof String) {
            if (!isSpecified((String) value)) {
                validator.newFinding(modelElement, Severity.ERROR, org.gedcom4j.validate.ProblemCode.MISSING_REQUIRED_VALUE,
                        fieldName);
            }
        } else if (value instanceof StringWithCustomFacts) {
            StringWithCustomFacts swct = (StringWithCustomFacts) value;
            if (swct.getValue() != null && !isSpecified(swct.getValue())) {
                validator.newFinding(modelElement, Severity.ERROR, org.gedcom4j.validate.ProblemCode.MISSING_REQUIRED_VALUE,
                        fieldName);
            }
        }
        if (modelElement instanceof HasCustomFacts) {
            checkCustomFacts((HasCustomFacts) modelElement);
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
                validator.newFinding(modelElement, Severity.ERROR, org.gedcom4j.validate.ProblemCode.MISSING_REQUIRED_VALUE,
                        fieldName);
            }
        } else if (value instanceof StringWithCustomFacts) {
            StringWithCustomFacts swct = (StringWithCustomFacts) value;
            if (swct.getValue() == null || !isSpecified(swct.getValue())) {
                validator.newFinding(modelElement, Severity.ERROR, org.gedcom4j.validate.ProblemCode.MISSING_REQUIRED_VALUE,
                        fieldName);
            }
        } else {
            throw new ValidationException("Don't know how to handle result of type " + value.getClass().getName());
        }
        if (modelElement instanceof HasCustomFacts) {
            checkCustomFacts((HasCustomFacts) modelElement);
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
        validator.newFinding(modelElement, Severity.ERROR, org.gedcom4j.validate.ProblemCode.ILLEGAL_VALUE, fieldName);
    }

    /**
     * Create a finding - automatically adds to the results.
     * 
     * @param itemOfConcern
     *            the item of concern. Required.
     * @param severity
     *            the severity. Required.
     * @param problemCode
     *            the problem code. Required.
     * @param fieldNameOfConcern
     *            the name of the field that has a problmatic value. Optional, but if supplied it needs to exist on the item of
     *            concern as a field
     * @throws IllegalArgumentException
     *             if any of the arguments are null
     * @return the finding just created and added to the results
     */
    protected Finding newFinding(ModelElement itemOfConcern, Severity severity, ProblemCode problemCode,
            String fieldNameOfConcern) {
        return validator.newFinding(itemOfConcern, severity, problemCode, fieldNameOfConcern);
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
            validator.newFinding(objectContainingXref, Severity.ERROR, org.gedcom4j.validate.ProblemCode.MISSING_REQUIRED_VALUE,
                    "xref");
        } else {
            if (!XREF_PATTERN.matcher(xref).matches()) {
                validator.newFinding(objectContainingXref, Severity.ERROR, org.gedcom4j.validate.ProblemCode.XREF_INVALID, "xref");
            }
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
    private Constructor<ModelElement> getCopyConstructor(ModelElement modelElement) {
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
     * @param object
     *            the object that has the named field you want a getter for
     * @param fieldName
     *            the name of the field you want to get
     * @return the getter method
     */
    @SuppressWarnings("PMD.PreserveStackTrace")
    private Method getGetter(Object object, String fieldName) {
        Method result = null;
        try {
            String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            result = object.getClass().getMethod(getterName);
        } catch (@SuppressWarnings("unused") NoSuchMethodException | SecurityException ignored) {
            try {
                String getterName = "is" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                result = object.getClass().getMethod(getterName);
            } catch (NoSuchMethodException | SecurityException e1) {
                throw new ValidationException("Unable to find getter method for field '" + fieldName + "' on object of type "
                        + object.getClass().getName(), e1);
            }
        }
        return result;
    }

}
