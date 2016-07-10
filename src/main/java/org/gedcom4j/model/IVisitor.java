package org.gedcom4j.model;

/**
 * Interface for implementing the visitor pattern. Classes that want to visit the object model to do various things with
 * it should implement this interface, and override/implement the visit() method (polymorphically).
 * 
 * @author frizbog
 */
public interface IVisitor {
    /**
     * Tell the visitor to visit an address
     * 
     * @param address
     *            the address
     */
    void visit(Address address);

    /**
     * Tell the visitor to visit an association
     * 
     * @param association
     *            the association
     */
    void visit(Association association);

    /**
     * Tell the visitor to visit a change date
     * 
     * @param changeDate
     *            the change date to visit
     */
    void visit(ChangeDate changeDate);

    /**
     * Tell the visitor to visit a character set
     * 
     * @param characterSet
     *            the character set to visit
     */
    void visit(CharacterSet characterSet);

    /**
     * Tell the visitor to visit a citation data object
     * 
     * @param citationData
     *            the citation data to visit
     */
    void visit(CitationData citationData);

    /**
     * Tell the visitor to visit a citation without source
     * 
     * @param citationWithoutSource
     *            the citation without source to visit
     */
    void visit(CitationWithoutSource citationWithoutSource);

    /**
     * Tell the visitor to visit a citation with source
     * 
     * @param citationWithSource
     *            the citation with source to visit
     */
    void visit(CitationWithSource citationWithSource);

    /**
     * Tell the visitor to visit a corporation
     * 
     * @param corporation
     *            the corporation to visit
     */
    void visit(Corporation corporation);

    /**
     * Tell the visitor to visit a recorded event
     * 
     * @param eventRecorded
     *            the recorded event to visit
     */
    void visit(EventRecorded eventRecorded);

    /**
     * Tell the visitor to visit a family
     * 
     * @param family
     *            the family to visit
     */
    void visit(Family family);

    /**
     * Tell the visitor to visit a family child
     * 
     * @param familyChild
     *            the family child to visit
     */
    void visit(FamilyChild familyChild);

    /**
     * Tell the visitor to visit a family event
     * 
     * @param familyEvent
     *            the family event to visit
     */
    void visit(FamilyEvent familyEvent);

    /**
     * Tell the visitor to visit a family spouse
     * 
     * @param familySpouse
     *            the family spouse to visit
     */
    void visit(FamilySpouse familySpouse);

    /**
     * Tell the visitor to visit a file reference
     * 
     * @param fileReference
     *            the file reference to visit
     */
    void visit(FileReference fileReference);

    /**
     * Tell the visitor to visit a gedcom
     * 
     * @param g
     *            the gedcom to visit
     */
    void visit(Gedcom g);

    /**
     * Tell the visitor to visit a gedcom version
     * 
     * @param gedcomVersion
     *            the gedcom version to visit
     */
    void visit(GedcomVersion gedcomVersion);

    /**
     * Tell the visitor to visit a header
     * 
     * @param h
     *            the header to visit
     */
    void visit(Header h);

    /**
     * Visit header source data
     * 
     * @param headerSourceData
     *            the header source data to visit
     */
    void visit(HeaderSourceData headerSourceData);

    /**
     * Tell the visitor to visit an individual
     * 
     * @param i
     *            the individual to visit
     */
    void visit(Individual i);

    /**
     * Tell the visitor to visit an individual attribute
     * 
     * @param individualAttribute
     *            the individual attribute to visit
     */
    void visit(IndividualAttribute individualAttribute);

    /**
     * Tell the visitor to visit an individual event
     * 
     * @param individualEvent
     *            the individual event to visit
     */
    void visit(IndividualEvent individualEvent);

    /**
     * Tell the visitor to visit an LDS individual ordinance
     * 
     * @param ldsIndividualOrdinance
     *            the ordinance to visit
     */
    void visit(LdsIndividualOrdinance ldsIndividualOrdinance);

    /**
     * Tell the visitor to visit an LDS spouse sealing
     * 
     * @param ldsSpouseSealing
     *            the sealing to visit
     */
    void visit(LdsSpouseSealing ldsSpouseSealing);

    /**
     * Tell the visitor to visit a multimedia object
     * 
     * @param multimedia
     *            the multimedia object to visit
     */
    void visit(Multimedia multimedia);

    /**
     * Tell the visitor to visit a note
     * 
     * @param note
     *            the note to visit
     */
    void visit(Note note);

    /**
     * Tell the visitor to visit a personal name
     * 
     * @param personalName
     *            the name to visit
     */
    void visit(PersonalName personalName);

    /**
     * Tell the visitor to visit a personal name variation
     * 
     * @param personalNameVariation
     *            the name variation to visit
     */
    void visit(PersonalNameVariation personalNameVariation);

    /**
     * Tell the visitor to visit a place
     * 
     * @param place
     *            the place to visit
     */
    void visit(Place place);

    /**
     * Tell the visitor to visit a repository
     * 
     * @param r
     *            the repository to visit
     */
    void visit(Repository r);

    /**
     * Tell the visitor to visit a repository citation
     * 
     * @param repositoryCitation
     *            the repository citation to visit
     */
    void visit(RepositoryCitation repositoryCitation);

    /**
     * Tell the visitor to visit a source
     * 
     * @param s
     *            the source to visit
     */
    void visit(Source s);

    /**
     * Tell the visitor to visit a source call number
     * 
     * @param sourceCallNumber
     *            the source call number to visit
     */
    void visit(SourceCallNumber sourceCallNumber);

    /**
     * Visit source data
     * 
     * @param sourceData
     *            the source data to visit
     */
    void visit(SourceData sourceData);

    /**
     * Tell the visitor to visit a source system
     * 
     * @param sourceSystem
     *            the source system to visit
     */
    void visit(SourceSystem sourceSystem);

    /**
     * Tell the visitor to visit a StringWithCustomTags
     * 
     * @param stringWithCustomTags
     *            the {@link StringWithCustomTags} to visit
     */
    void visit(StringWithCustomTags stringWithCustomTags);

    /**
     * Tell the visitor to visit a submission
     * 
     * @param submission
     *            the submission to visit
     */
    void visit(Submission submission);

    /**
     * Tell the visitor to visit a submitter
     * 
     * @param submitter
     *            the submitter to visit
     */
    void visit(Submitter submitter);

    /**
     * Tell the visitor to visit a user reference
     * 
     * @param userReference
     *            to visit
     */
    void visit(UserReference userReference);
}
