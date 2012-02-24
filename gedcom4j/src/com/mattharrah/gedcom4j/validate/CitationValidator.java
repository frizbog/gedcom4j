package com.mattharrah.gedcom4j.validate;

import java.util.ArrayList;
import java.util.List;

import com.mattharrah.gedcom4j.model.AbstractCitation;
import com.mattharrah.gedcom4j.model.CitationWithSource;
import com.mattharrah.gedcom4j.model.CitationWithoutSource;
import com.mattharrah.gedcom4j.model.Note;

/**
 * A validator for source citations - both {@link CitationWithoutSource} and
 * {@link CitationWithSource}. See {@link GedcomValidator} for usage
 * information.
 * 
 * @author frizbog1
 * 
 */
public class CitationValidator extends AbstractValidator {

    /**
     * The citation being validated
     */
    AbstractCitation citation;

    /**
     * Constructor
     * 
     * @param rootValidator
     *            the root validator with the collection of findings
     * @param citation
     *            the citation being validated
     */
    public CitationValidator(GedcomValidator rootValidator,
            AbstractCitation citation) {
        this.rootValidator = rootValidator;
        this.citation = citation;
    }

    @Override
    protected void validate() {
        if (citation == null) {
            addError("Citation is null");
            return;
        }
        if (citation instanceof CitationWithSource) {
            CitationWithSource c = (CitationWithSource) citation;
            if (c.source == null) {
                addError(
                        "CitationWithSource requires a non-null source reference",
                        c);
            }
            checkOptionalString(c.whereInSource, "where within source", c);
            checkOptionalString(c.eventCited, "event type cited from", c);
            if (c.eventCited != null) {
                checkOptionalString(c.roleInEvent, "role in event", c);
            } else {
                if (c.roleInEvent != null) {
                    addError("CitationWithSource has role in event but a null event");
                }
            }
            checkOptionalString(c.certainty, "certainty/quality", c);
        } else if (citation instanceof CitationWithoutSource) {
            CitationWithoutSource c = (CitationWithoutSource) citation;
            checkStringList(c.description,
                    "description on a citation without a source", true);
            if (c.textFromSource == null) {
                if (rootValidator.autorepair) {
                    c.textFromSource = new ArrayList<List<String>>();
                    addInfo("Text from source collection (the list of lists) was null in CitationWithoutSource - autorepaired",
                            citation);
                } else {
                    addError(
                            "Text from source collection (the list of lists) is null in CitationWithoutSource",
                            citation);
                }
            } else {
                for (List<String> sl : c.textFromSource) {
                    if (sl == null) {
                        addError(
                                "Text from source collection (the list of lists) in CitationWithoutSource contains a null",
                                citation);
                    } else {
                        checkStringList(
                                sl,
                                "one of the sublists in the textFromSource collection on a source citation",
                                true);
                    }
                }
            }
        } else {
            throw new IllegalStateException(
                    "AbstractCitation references must be either CitationWithSource"
                            + " instances or CitationWithoutSource instances");
        }
        if (citation.notes == null) {
            if (rootValidator.autorepair) {
                citation.notes = new ArrayList<Note>();
                addInfo("Notes collection was null on "
                        + citation.getClass().getSimpleName()
                        + " - autorepaired");
            } else {
                addError("Notes collection is null on "
                        + citation.getClass().getSimpleName());
            }
        } else {
            checkNotes(citation.notes, citation);

        }

    }
}
