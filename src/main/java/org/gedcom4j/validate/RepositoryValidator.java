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

import org.gedcom4j.model.Address;
import org.gedcom4j.model.Repository;

/**
 * A validator for a {@link Repository} structure. See {@link Validator} for usage information.
 * 
 * @author frizbog1
 */
class RepositoryValidator extends AbstractValidator {
    /**
     * The individul being validated
     */
    private final Repository repository;

    /**
     * Constructor
     * 
     * @param gedcomValidator
     *            the root validator
     * @param repository
     *            the repository being validated
     */
    RepositoryValidator(Validator validator, Repository repository) {
        this.validator = validator;
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        if (repository == null) {
            addError("Repository being validated is null");
            return;
        }
        checkXref(repository);
        mustHaveValueOrBeOmitted(repository, "name");
        checkChangeDate(repository.getChangeDate(), repository);
        checkStringTagList(repository.getEmails(), "email list", false);
        checkUserReferences(repository.getUserReferences(), repository);
        mustHaveValueOrBeOmitted(repository, "recIdNumber");
        checkStringTagList(repository.getPhoneNumbers(), "phone numbers", false);
        new NotesListValidator(validator, repository).validate();

        Address a = repository.getAddress();
        if (a != null) {
            new AddressValidator(validator, a).validate();
        }

    }
}
