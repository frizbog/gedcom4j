package org.gedcom4j.validate;

import org.gedcom4j.Options;
import org.junit.Test;

/**
 * Test for {@link NotesListValidator}
 * 
 * @author frizbog
 *
 */
public class NotesListValidatorTest extends AbstractValidatorTestCase {

    /**
     * Test when the notes list is null/uninitialized
     */
    @Test
    public void testNullNotesList() {
        Options.setCollectionInitializationEnabled(true);
        new NotesListValidator(validator, gedcom.getHeader()).validate();
        assertFindingsContain(Severity.INFO, gedcom.getHeader(), ProblemCode.UNINITIALIZED_COLLECTION.getCode(), "notes");
        Options.setCollectionInitializationEnabled(false);
    }

}
