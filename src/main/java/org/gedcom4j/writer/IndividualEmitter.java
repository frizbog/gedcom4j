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
package org.gedcom4j.writer;

import java.util.Collection;
import java.util.List;

import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.model.Association;
import org.gedcom4j.model.FamilyChild;
import org.gedcom4j.model.FamilySpouse;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualAttribute;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.LdsIndividualOrdinance;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.PersonalNameVariation;
import org.gedcom4j.model.StringWithCustomFacts;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.model.UserReference;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.model.enumerations.LdsIndividualOrdinanceType;

/**
 * @author frizbog
 *
 */
@SuppressWarnings("PMD.GodClass")
class IndividualEmitter extends AbstractEmitter<Collection<Individual>> {

    /**
     * Constructor
     * 
     * @param baseWriter
     *            The base Gedcom writer class this Emitter is partnering with to emit the entire file
     * @param startLevel
     *            write starting at this level
     * @param writeFrom
     *            object to write
     * @throws WriterCancelledException
     *             if cancellation was requested during the operation
     */
    IndividualEmitter(GedcomWriter baseWriter, int startLevel, Collection<Individual> writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void emit() throws GedcomWriterException {
        if (writeFrom == null) {
            return;
        }
        for (Individual i : writeFrom) {
            emitTag(0, i.getXref(), "INDI");
            emitTagIfValueNotNull(1, "RESN", i.getRestrictionNotice());
            emitPersonalNames(1, i.getNames());
            emitTagIfValueNotNull(1, "SEX", i.getSex());
            emitIndividualEvents(1, i.getEvents());
            emitIndividualAttributes(1, i.getAttributes());
            emitLdsIndividualOrdinances(1, i.getLdsIndividualOrdinances());
            emitChildToFamilyLinks(1, i);
            emitSpouseInFamilyLinks(1, i);
            if (i.getSubmitters() != null) {
                for (Submitter s : i.getSubmitters()) {
                    emitTagWithRequiredValue(1, "SUBM", s.getXref());
                }
            }
            emitAssociationStructures(1, i.getAssociations());
            if (i.getAliases() != null) {
                for (StringWithCustomFacts s : i.getAliases()) {
                    emitTagWithRequiredValue(1, "ALIA", s);
                }
            }
            if (i.getAncestorInterest() != null) {
                for (Submitter s : i.getAncestorInterest()) {
                    emitTagWithRequiredValue(1, "ANCI", s.getXref());
                }
            }
            if (i.getDescendantInterest() != null) {
                for (Submitter s : i.getDescendantInterest()) {
                    emitTagWithRequiredValue(1, "DESI", s.getXref());
                }
            }
            new SourceCitationEmitter(baseWriter, 1, i.getCitations()).emit();
            new MultimediaLinksEmitter(baseWriter, 1, i.getMultimedia()).emit();
            new NotesEmitter(baseWriter, 1, i.getNotes()).emit();
            emitTagIfValueNotNull(1, "RFN", i.getPermanentRecFileNumber());
            emitTagIfValueNotNull(1, "AFN", i.getAncestralFileNumber());
            if (i.getUserReferences() != null) {
                for (UserReference u : i.getUserReferences()) {
                    emitTagWithRequiredValue(1, "REFN", u.getReferenceNum());
                    emitTagIfValueNotNull(2, "TYPE", u.getType());
                }
            }
            emitTagIfValueNotNull(1, "RIN", i.getRecIdNumber());
            new ChangeDateEmitter(baseWriter, 1, i.getChangeDate()).emit();
            emitCustomFacts(1, i.getCustomFacts());
        }

    }

    /**
     * Emit the person-to-person associations an individual was in - see ASSOCIATION_STRUCTURE in the GEDCOM spec.
     * 
     * @param level
     *            the level at which to start recording
     * @param associations
     *            the list of associations
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitAssociationStructures(int level, List<Association> associations) throws GedcomWriterException {
        if (associations != null) {
            for (Association a : associations) {
                emitTagWithRequiredValue(level, "ASSO", a.getAssociatedEntityXref());
                emitTagWithRequiredValue(level + 1, "TYPE", a.getAssociatedEntityType());
                emitTagWithRequiredValue(level + 1, "RELA", a.getRelationship());
                new NotesEmitter(baseWriter, level + 1, a.getNotes()).emit();
                new SourceCitationEmitter(baseWriter, level + 1, a.getCitations()).emit();
                emitCustomFacts(level + 1, a.getCustomFacts());
            }
        }
    }

    /**
     * Emit links to all the families to which this individual was a child
     * 
     * @param level
     *            the level in the hierarchy at which we are emitting
     * @param i
     *            the individual we're dealing with
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitChildToFamilyLinks(int level, Individual i) throws GedcomWriterException {
        if (i.getFamiliesWhereChild() != null) {
            for (FamilyChild familyChild : i.getFamiliesWhereChild()) {
                if (familyChild == null) {
                    throw new GedcomWriterException("Family to which " + i + " was a child was null");
                }
                if (familyChild.getFamily() == null) {
                    throw new GedcomWriterException("Family to which " + i + " was a child had a null family reference");
                }
                emitTagWithRequiredValue(level, "FAMC", familyChild.getFamily().getXref());
                emitTagIfValueNotNull(level + 1, "PEDI", familyChild.getPedigree());
                emitTagIfValueNotNull(level + 1, "STAT", familyChild.getStatus());
                new NotesEmitter(baseWriter, level + 1, familyChild.getNotes()).emit();
                emitCustomFacts(level + 1, i.getCustomFacts());
            }
        }
    }

    /**
     * Emit a collection of individual attributes
     * 
     * @param level
     *            the level to start emitting at
     * @param attributes
     *            the attributes
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitIndividualAttributes(int level, List<IndividualAttribute> attributes) throws GedcomWriterException {
        if (attributes != null) {
            for (IndividualAttribute a : attributes) {
                emitTagWithOptionalValueAndCustomSubtags(level, a.getType().getTag(), a.getDescription());
                new EventEmitter(baseWriter, level + 1, a).emit();
                new AddressEmitter(baseWriter, level + 1, a.getAddress()).emit();
                emitStringsWithCustomTags(level + 1, a.getPhoneNumbers(), "PHON");
                emitStringsWithCustomTags(level + 1, a.getWwwUrls(), "WWW");
                emitStringsWithCustomTags(level + 1, a.getFaxNumbers(), "FAX");
                emitStringsWithCustomTags(level + 1, a.getEmails(), "EMAIL");
            }
        }
    }

    /**
     * Emit a collection of individual events
     * 
     * @param level
     *            the level to start emitting at
     * @param events
     *            the events
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitIndividualEvents(int level, List<IndividualEvent> events) throws GedcomWriterException {
        if (events != null) {
            for (IndividualEvent e : events) {
                emitTagWithOptionalValue(level, e.getType().getTag(), e.getYNull());
                new EventEmitter(baseWriter, level + 1, e).emit();
                if (e.getType() == IndividualEventType.BIRTH || e.getType() == IndividualEventType.CHRISTENING) {
                    if (e.getFamily() != null && e.getFamily().getFamily() != null && e.getFamily().getFamily().getXref() != null) {
                        emitTagWithRequiredValue(level + 1, "FAMC", e.getFamily().getFamily().getXref());
                    }
                } else if (e.getType() == IndividualEventType.ADOPTION && e.getFamily() != null && e.getFamily().getFamily() != null
                        && e.getFamily().getFamily().getXref() != null) {
                    emitTagWithRequiredValue(level + 1, "FAMC", e.getFamily().getFamily().getXref());
                    emitTagIfValueNotNull(level + 2, "ADOP", e.getFamily().getAdoptedBy());
                }
            }
        }
    }

    /**
     * Emit the LDS individual ordinances
     * 
     * @param level
     *            the level in the hierarchy we are processing
     * @param ldsIndividualOrdinances
     *            the list of LDS individual ordinances to emit
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitLdsIndividualOrdinances(int level, List<LdsIndividualOrdinance> ldsIndividualOrdinances)
            throws GedcomWriterException {
        if (ldsIndividualOrdinances != null) {
            for (LdsIndividualOrdinance o : ldsIndividualOrdinances) {
                emitTagWithOptionalValue(level, o.getType().getTag(), o.getYNull());
                emitTagIfValueNotNull(level + 1, "STAT", o.getStatus());
                emitTagIfValueNotNull(level + 1, "DATE", o.getDate());
                emitTagIfValueNotNull(level + 1, "TEMP", o.getTemple());
                emitTagIfValueNotNull(level + 1, "PLAC", o.getPlace());
                if (o.getType() == LdsIndividualOrdinanceType.CHILD_SEALING) {
                    if (o.getFamilyWhereChild() == null) {
                        throw new GedcomWriterException("LDS Ordinance info for a child sealing had no reference to a family");
                    }
                    if (o.getFamilyWhereChild().getFamily() == null) {
                        throw new GedcomWriterException(
                                "LDS Ordinance info for a child sealing had familyChild object with a null reference to a family");
                    }
                    emitTagWithRequiredValue(level + 1, "FAMC", o.getFamilyWhereChild().getFamily().getXref());
                }
                new SourceCitationEmitter(baseWriter, level + 1, o.getCitations()).emit();
                new NotesEmitter(baseWriter, level + 1, o.getNotes()).emit();
                emitCustomFacts(level + 1, o.getCustomFacts());
            }
        }
    }

    /**
     * Emit a list of personal names for an individual
     * 
     * @param level
     *            the level to start emitting at
     * @param names
     *            the names
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitPersonalNames(int level, List<PersonalName> names) throws GedcomWriterException {
        if (names != null) {
            for (PersonalName n : names) {
                emitTagWithOptionalValue(level, "NAME", n.getBasic());
                emitTagIfValueNotNull(level + 1, "NPFX", n.getPrefix());
                emitTagIfValueNotNull(level + 1, "GIVN", n.getGivenName());
                emitTagIfValueNotNull(level + 1, "NICK", n.getNickname());
                emitTagIfValueNotNull(level + 1, "SPFX", n.getSurnamePrefix());
                emitTagIfValueNotNull(level + 1, "SURN", n.getSurname());
                emitTagIfValueNotNull(level + 1, "NSFX", n.getSuffix());
                if (n.getRomanized() != null) {
                    for (PersonalNameVariation pnv : n.getRomanized()) {
                        emitPersonalNameVariation(level + 1, "ROMN", pnv);
                    }
                }
                if (n.getPhonetic() != null) {
                    for (PersonalNameVariation pnv : n.getPhonetic()) {
                        emitPersonalNameVariation(level + 1, "FONE", pnv);
                    }
                }
                new SourceCitationEmitter(baseWriter, level + 1, n.getCitations()).emit();
                new NotesEmitter(baseWriter, level + 1, n.getNotes()).emit();
                emitCustomFacts(level + 1, n.getCustomFacts());
            }
        }
    }

    /**
     * Emit a personal name variation - either romanized or phonetic
     * 
     * @param level
     *            - the level we are writing at
     * @param variationTag
     *            the tag for the type of variation - should be "ROMN" for romanized, or "FONE" for phonetic
     * @param pnv
     *            - the personal name variation we are writing
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitPersonalNameVariation(int level, String variationTag, PersonalNameVariation pnv) throws GedcomWriterException {
        emitTagWithRequiredValue(level, variationTag, pnv.getVariation());
        emitTagIfValueNotNull(level + 1, "NPFX", pnv.getPrefix());
        emitTagIfValueNotNull(level + 1, "GIVN", pnv.getGivenName());
        emitTagIfValueNotNull(level + 1, "NICK", pnv.getNickname());
        emitTagIfValueNotNull(level + 1, "SPFX", pnv.getSurnamePrefix());
        emitTagIfValueNotNull(level + 1, "SURN", pnv.getSurname());
        emitTagIfValueNotNull(level + 1, "NSFX", pnv.getSuffix());
        new SourceCitationEmitter(baseWriter, level + 1, pnv.getCitations()).emit();
        new NotesEmitter(baseWriter, level + 1, pnv.getNotes()).emit();
        emitCustomFacts(level + 1, pnv.getCustomFacts());
    }

    /**
     * Emit links to all the families to which the supplied individual was a spouse
     * 
     * @param level
     *            the level in the hierarchy at which we are emitting
     * @param i
     *            the individual we are dealing with
     * @throws GedcomWriterException
     *             if data is malformed and cannot be written
     */
    private void emitSpouseInFamilyLinks(int level, Individual i) throws GedcomWriterException {
        if (i.getFamiliesWhereSpouse() != null) {
            for (FamilySpouse familySpouse : i.getFamiliesWhereSpouse()) {
                if (familySpouse == null) {
                    throw new GedcomWriterException("Family in which " + i + " was a spouse was null");
                }
                if (familySpouse.getFamily() == null) {
                    throw new GedcomWriterException("Family in which " + i + " was a spouse had a null family reference");
                }
                emitTagWithRequiredValue(level, "FAMS", familySpouse.getFamily().getXref());
                new NotesEmitter(baseWriter, level + 1, familySpouse.getNotes()).emit();
                emitCustomFacts(level + 1, familySpouse.getCustomFacts());
            }
        }
    }

}
