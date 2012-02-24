package com.mattharrah.gedcom4j.validate;

import com.mattharrah.gedcom4j.model.Address;
import com.mattharrah.gedcom4j.model.Repository;

/**
 * A validator for a {@link Repository} structure. See {@link GedcomValidator}
 * for usage information.
 * 
 * @author frizbog1
 */
public class RepositoryValidator extends AbstractValidator {
    /**
     * The individul being validated
     */
    private Repository repository;

    /**
     * Constructor
     * 
     * @param gedcomValidator
     *            the root validator
     * @param repository
     *            the repository being validated
     */
    public RepositoryValidator(GedcomValidator gedcomValidator,
            Repository repository) {
        rootValidator = gedcomValidator;
        this.repository = repository;
    }

    @Override
    protected void validate() {
        if (repository == null) {
            addError("Repository being validated is null");
            return;
        }
        checkXref(repository);
        checkOptionalString(repository.name, "name", repository);
        checkChangeDate(repository.changeDate, repository);
        checkStringList(repository.emails, "email list", false);
        checkUserReferences(repository.userReferences, repository);
        checkOptionalString(repository.recIdNumber, "automated record id",
                repository);
        checkStringList(repository.phoneNumbers, "phone numbers", false);
        checkNotes(repository.notes, repository);

        Address a = repository.address;
        new AddressValidator(rootValidator, a).validate();

    }
}
