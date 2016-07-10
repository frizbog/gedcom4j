package org.gedcom4j.validator;

import org.gedcom4j.model.*;

/**
 * A validator for a {@link Gedcom} object. Traverses and validates the entire structure.
 * 
 * @author frizbog
 */
public class GedcomValidator extends AbstractValidator<Gedcom> {

    /**
     * Constructor
     * 
     * @param validationVisitor
     *            The validation visitor that will visit and validate child elements
     * @param g
     *            The gedcom being validated
     */
    public GedcomValidator(ValidationVisitor validationVisitor, Gedcom g) {
        super(validationVisitor, g);
    }

    /**
     * Do the validation
     */
    @Override
    protected void validate() {
        System.out.println("Validating gedcom");
        if (object == null) {
            validationVisitor.getRootValidator().addError("Gedcom structure is null");
            return;
        }
        validationVisitor.getRootValidator().validateCustomTags(object.getCustomTags());

        new HeaderValidator(validationVisitor, object.getHeader()).validate();
        object.getHeader().accept(validationVisitor);
        object.getSubmission().accept(validationVisitor);
        for (Individual i : object.getIndividuals().values()) {
            new IndividualValidator(validationVisitor, i).validate();
        }
        for (Family f : object.getFamilies().values()) {
            new FamilyValidator(validationVisitor, f).validate();
        }
        for (Multimedia m : object.getMultimedia().values()) {
            new MultimediaValidator(validationVisitor, m).validate();
        }
        for (Note n : object.getNotes().values()) {
            new NoteValidator(validationVisitor, n).validate();
        }
        for (Repository r : object.getRepositories().values()) {
            new RepositoryValidator(validationVisitor, r).validate();
        }
        for (Source s : object.getSources().values()) {
            new SourceValidator(validationVisitor, s).validate();
        }
        for (Submitter s : object.getSubmitters().values()) {
            new SubmitterValidator(validationVisitor, s).validate();
        }
        new TrailerValidator(validationVisitor, object.getTrailer()).validate();
    }
}
