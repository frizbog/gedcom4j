/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation parses (the "Software"), to deal
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
package org.gedcom4j.parser.event;

import java.util.EventObject;

import org.gedcom4j.model.Gedcom;

/**
 * An event to hold information about parsing progress.
 * 
 * @author frizbog
 */
public class ParseProgressEvent extends EventObject {

    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 996714620347714206L;

    /**
     * Is the parse completely processed?
     */
    private final boolean complete;

    /**
     * Number of families processed
     */
    private final int familiesProcessed;

    /**
     * Number of individuals processed
     */
    private final int individualsProcessed;

    /**
     * Number of multimedia processed
     */
    private final int multimediaProcessed;

    /**
     * Number of notes processed
     */
    private final int notesProcessed;

    /**
     * Number of families processed
     */
    private final int repositoriesProcessed;

    /**
     * Number of sources processed
     */
    private final int sourcesProcessed;

    /**
     * Number of submitters processed
     */
    private final int submittersProcessed;

    /**
     * Constructor
     * 
     * @param source
     *            the source object
     * @param g
     *            the gedcom being loaded - this is used for the source of the progress data
     * @param complete
     *            is the parse complete
     */
    public ParseProgressEvent(Object source, Gedcom g, boolean complete) {
        super(source);
        familiesProcessed = g.families.size();
        individualsProcessed = g.individuals.size();
        multimediaProcessed = g.multimedia.size();
        notesProcessed = g.notes.size();
        repositoriesProcessed = g.repositories.size();
        sourcesProcessed = g.sources.size();
        submittersProcessed = g.submitters.size();
        this.complete = complete;
    }

    /**
     * Get number of families processed
     * 
     * @return number of families processed
     */
    public int getFamiliesProcessed() {
        return familiesProcessed;
    }

    /**
     * Get number of individuals processed
     * 
     * @return number of individuals processed
     */
    public int getIndividualsProcessed() {
        return individualsProcessed;
    }

    /**
     * Get number of multimedia processed
     * 
     * @return number of multimedia processed
     */
    public int getMultimediaProcessed() {
        return multimediaProcessed;
    }

    /**
     * Get number of notes processed
     * 
     * @return number of notes processed
     */
    public int getNotesProcessed() {
        return notesProcessed;
    }

    /**
     * Get number of repositories processed
     * 
     * @return number of repositories processed
     */
    public int getRepositoriesProcessed() {
        return repositoriesProcessed;
    }

    /**
     * Get number of sources processed
     * 
     * @return number of sources processed
     */
    public int getSourcesProcessed() {
        return sourcesProcessed;
    }

    /**
     * Get number of submitters processed
     * 
     * @return number of submitters processed
     */
    public int getSubmittersProcessed() {
        return submittersProcessed;
    }

    /**
     * Is the parse operation complete?
     * 
     * @return true if the pars operation is complete
     */
    public boolean isComplete() {
        return complete;
    }

    @Override
    public String toString() {
        return "ParseProgressEvent [complete=" + complete + ", familiesProcessed=" + familiesProcessed + ", individualsProcessed=" + individualsProcessed
                + ", multimediaProcessed=" + multimediaProcessed + ", notesProcessed=" + notesProcessed + ", repositoriesProcessed=" + repositoriesProcessed
                + ", sourcesProcessed=" + sourcesProcessed + ", submittersProcessed=" + submittersProcessed + "]";
    }

}
