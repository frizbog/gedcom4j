/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.gedcom4j.validator;

import org.gedcom4j.model.*;

/**
 * Class that dispatches all the validation callbacks from the visited classes to delegate classes that perform the
 * actual validation
 * 
 * @author frizbog
 */
class ValidationVisitor implements IVisitor {

    /**
     * The root validator class that holds things like error messages, etc.
     */
    private final Validator rootValidator;

    /**
     * Default constructor
     * 
     * @param v
     *            the root validator
     */
    public ValidationVisitor(Validator v) {
        rootValidator = v;
    }

    /**
     * Get the rootValidator
     * 
     * @return the rootValidator
     */
    public Validator getRootValidator() {
        return rootValidator;
    }

    @Override
    public void visit(Address a) {
        System.out.println("Validating " + a.getClass().getName() + ": " + a);
    }

    @Override
    public void visit(Association a) {
        System.out.println("Validating " + a.getClass().getName() + ": " + a);
    }

    /**
     * Validate the ChangeDate
     * 
     * @param c
     *            the ChangeDate object
     */
    public void visit(ChangeDate c) {
        System.out.println("Validating " + c.getClass().getName() + ": " + c);
    }

    @Override
    public void visit(CharacterSet c) {
        System.out.println("Validating " + c.getClass().getName() + ": " + c);
    }

    @Override
    public void visit(CitationData c) {
        System.out.println("Validating " + c.getClass().getName() + ": " + c);
    }

    @Override
    public void visit(CitationWithoutSource c) {
        System.out.println("Validating " + c.getClass().getName() + ": " + c);
    }

    @Override
    public void visit(CitationWithSource c) {
        System.out.println("Validating " + c.getClass().getName() + ": " + c);
    }

    @Override
    public void visit(Corporation c) {
        System.out.println("Validating " + c.getClass().getName() + ": " + c);
    }

    @Override
    public void visit(EventRecorded e) {
        System.out.println("Validating " + e.getClass().getName() + ": " + e);
    }

    @Override
    public void visit(Family f) {
        System.out.println("Validating " + f.getClass().getName() + ": " + f);
    }

    @Override
    public void visit(FamilyChild f) {
        System.out.println("Validating " + f.getClass().getName() + ": " + f);
    }

    @Override
    public void visit(FamilyEvent f) {
        System.out.println("Validating " + f.getClass().getName() + ": " + f);
    }

    @Override
    public void visit(FamilySpouse f) {
        System.out.println("Validating " + f.getClass().getName() + ": " + f);
    }

    @Override
    public void visit(FileReference f) {
        System.out.println("Validating " + f.getClass().getName() + ": " + f);
    }

    @Override
    public void visit(Gedcom g) {
        new GedcomValidator(this, g).validate();
    }

    @Override
    public void visit(GedcomVersion g) {
        System.out.println("Validating " + g.getClass().getName() + ": " + g);
    }

    @Override
    public void visit(Header h) {
        System.out.println("Validating " + h.getClass().getName() + ": " + h);
    }

    @Override
    public void visit(HeaderSourceData h) {
        System.out.println("Validating " + h.getClass().getName() + ": " + h);
    }

    @Override
    public void visit(Individual i) {
        System.out.println("Validating " + i.getClass().getName() + ": " + i);
    }

    @Override
    public void visit(IndividualAttribute i) {
        System.out.println("Validating " + i.getClass().getName() + ": " + i);
    }

    @Override
    public void visit(IndividualEvent i) {
        System.out.println("Validating " + i.getClass().getName() + ": " + i);
    }

    @Override
    public void visit(LdsIndividualOrdinance l) {
        System.out.println("Validating " + l.getClass().getName() + ": " + l);
    }

    @Override
    public void visit(LdsSpouseSealing l) {
        System.out.println("Validating " + l.getClass().getName() + ": " + l);
    }

    @Override
    public void visit(Multimedia m) {
        System.out.println("Validating " + m.getClass().getName() + ": " + m);
    }

    @Override
    public void visit(Note n) {
        System.out.println("Validating " + n.getClass().getName() + ": " + n);
    }

    @Override
    public void visit(PersonalName p) {
        System.out.println("Validating " + p.getClass().getName() + ": " + p);
    }

    @Override
    public void visit(PersonalNameVariation p) {
        System.out.println("Validating " + p.getClass().getName() + ": " + p);
    }

    @Override
    public void visit(Place p) {
        System.out.println("Validating " + p.getClass().getName() + ": " + p);
    }

    @Override
    public void visit(Repository r) {
        System.out.println("Validating " + r.getClass().getName() + ": " + r);
    }

    @Override
    public void visit(RepositoryCitation r) {
        System.out.println("Validating " + r.getClass().getName() + ": " + r);
    }

    @Override
    public void visit(Source s) {
        System.out.println("Validating " + s.getClass().getName() + ": " + s);
    }

    @Override
    public void visit(SourceCallNumber s) {
        System.out.println("Validating " + s.getClass().getName() + ": " + s);
    }

    @Override
    public void visit(SourceData s) {
        System.out.println("Validating " + s.getClass().getName() + ": " + s);
    }

    @Override
    public void visit(SourceSystem s) {
        System.out.println("Validating " + s.getClass().getName() + ": " + s);
    }

    @Override
    public void visit(StringWithCustomTags s) {
        System.out.println("Validating " + s.getClass().getName() + ": " + s);
    }

    @Override
    public void visit(Submission s) {
        System.out.println("Validating " + s.getClass().getName() + ": " + s);
    }

    @Override
    public void visit(Submitter s) {
        System.out.println("Validating " + s.getClass().getName() + ": " + s);
    }

    @Override
    public void visit(UserReference u) {
        System.out.println("Validating " + u.getClass().getName() + ": " + u);
    }

}
