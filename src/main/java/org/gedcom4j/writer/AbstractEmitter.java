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

package org.gedcom4j.writer;

import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.model.StringTree;
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.SupportedVersion;

import sun.security.krb5.internal.ccache.Tag;

/**
 * A base class for type-specific writers, which "render" the object model as GEDCOM text.
 * 
 * @author frizbog
 * @param <T>
 */
abstract class AbstractEmitter<T> {

    /**
     * The maximum length of lines to be written out before splitting
     */
    private static final int MAX_LINE_LENGTH = 128;

    /**
     * write starting at this level
     */
    protected int startLevel;

    /**
     * object to write
     */
    protected final T writeFrom;

    /**
     * The base Gedcom writer class this Emitter is partnering with to emit the entire file
     */
    protected GedcomWriter baseWriter;

    /**
     * Constructor
     * 
     * @param baseWriter
     *            The base Gedcom writer class this Emitter is partnering with to emit the entire file
     * @param startLevel
     *            write starting at this level
     * @param writeFrom
     *            object to write
     */
    protected AbstractEmitter(GedcomWriter baseWriter, int startLevel, T writeFrom) {
        this.baseWriter = baseWriter;
        this.startLevel = startLevel;
        this.writeFrom = writeFrom;
        if (baseWriter != null) {
            baseWriter.notifyConstructObserversIfNeeded();
        }
    }

    /**
     * Emit the GEDCOM text for the object this Emitter is for
     * 
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    protected abstract void emit() throws GedcomWriterException;

    /**
     * Convenience method for emitting lines of text when there is no xref, so you don't have to pass null all the time
     * 
     * @param level
     *            the level we are starting at. Continuation lines will be one level deeper than this value
     * @param startingTag
     *            the tag to use for the first line of the text. All subsequent lines will be "CONT" lines.
     * @param linesOfText
     *            the lines of text
     */
    protected void emitLinesOfText(int level, String startingTag, List<String> linesOfText) {
        emitLinesOfText(level, null, startingTag, linesOfText);
    }

    /**
     * Emit a multi-line text value. If a line of text contains line breaks (newlines, line feeds, carriage returns),
     * the parts on either side of the line break will be treated as separate lines.
     * 
     * @param level
     *            the level we are starting at. Continuation lines will be one level deeper than this value
     * @param startingTag
     *            the tag to use for the first line of the text. All subsequent lines will be "CONT" lines.
     * @param xref
     *            the xref of the item with lines of text
     * @param linesOfText
     *            the lines of text to write
     */
    protected void emitLinesOfText(int level, String xref, String startingTag, List<String> linesOfText) {
        List<String> splitLinesOfText = splitLinesOnBreakingCharacters(linesOfText);
        int lineNum = 0;
        for (String l : splitLinesOfText) {
            StringBuilder line = new StringBuilder();
            if (lineNum == 0) {
                line.append(level).append(" ");
                if (xref != null && xref.length() > 0) {
                    line.append(xref).append(" ");
                }
                line.append(startingTag).append(" ").append(l);
            } else {
                line.append(level + 1).append(" ");
                if (xref != null && xref.length() > 0) {
                    line.append(xref).append(" ");
                }
                line.append("CONT ").append(l);
            }
            lineNum++;
            emitAndSplit(level, line.toString());
        }
    }

    /**
     * Emit a list of {@link StringWithCustomTags} objects, using a specific {@link Tag} value.
     * 
     * @param level
     *            the level to write at
     * @param strings
     *            the list of strings to write (with custom tags)
     * @param tag
     *            the tag to use for each string value in the collection
     * @throws GedcomWriterException
     *             if the data cannot be written
     */
    protected void emitStringsWithCustomTags(int level, List<StringWithCustomTags> strings, String tagValue) throws GedcomWriterException {
        if (strings != null) {
            for (StringWithCustomTags f : strings) {
                emitTagWithRequiredValue(level, tagValue, f);
            }
        }
    }

    /**
     * Write a line with a tag, with no value following the tag
     * 
     * @param level
     *            the level within the file hierarchy
     * @param tag
     *            the tag for the line of the file
     */
    protected void emitTag(int level, String tag) {
        baseWriter.lines.add(level + " " + tag);
    }

    /**
     * Write a line with a tag, with no value following the tag
     * 
     * @param level
     *            the level within the file hierarchy
     * @param xref
     *            the xref of the item being written, if any
     * @param tag
     *            the tag for the line of the file
     */
    protected void emitTag(int level, String xref, String tag) {
        StringBuilder line = new StringBuilder(Integer.toString(level));
        if (xref != null && xref.length() > 0) {
            line.append(" ").append(xref);
        }
        line.append(" ").append(tag);
        baseWriter.lines.add(line.toString());
    }

    /**
     * Write a line if the value is non-null
     * 
     * @param level
     *            the level within the file hierarchy
     * @param tag
     *            the tag for the line of the file
     * @param value
     *            the value to write to the right of the tag
     */
    protected void emitTagIfValueNotNull(int level, String tag, Object value) {
        emitTagIfValueNotNull(level, null, tag, value);
    }

    /**
     * Write a line and tag, with an optional value for the tag
     * 
     * @param level
     *            the level within the file hierarchy
     * @param tag
     *            the tag for the line of the file
     * @param value
     *            the value to write to the right of the tag
     * @throws GedcomWriterException
     *             if the value is null or blank (which never happens, because we check for it)
     */
    protected void emitTagWithOptionalValue(int level, String tag, String value) throws GedcomWriterException {
        if (value == null) {
            StringBuilder line = new StringBuilder(Integer.toString(level));
            line.append(" ").append(tag);
            baseWriter.lines.add(line.toString());
        } else {
            List<String> temp = new ArrayList<String>();
            temp.add(value);
            List<String> valueLines = splitLinesOnBreakingCharacters(temp);

            boolean first = true;
            for (String v : valueLines) {

                StringBuilder line = new StringBuilder();
                if (first) {
                    line.append(level);
                    line.append(" ").append(tag).append(" ").append(v);
                    emitAndSplit(level, line.toString());
                } else {
                    line.append(level + 1);
                    line.append(" CONT ").append(v);
                    emitAndSplit(level + 1, line.toString());
                }

                first = false;
            }
        }
    }

    /**
     * Write a line and tag, with an optional value for the tag
     * 
     * @param level
     *            the level within the file hierarchy
     * @param tag
     *            the tag for the line of the file
     * @param valueToRightOfTag
     *            the value to write to the right of the tag
     * @throws GedcomWriterException
     *             if the value is null or blank (which never happens, because we check for it)
     */
    protected void emitTagWithOptionalValueAndCustomSubtags(int level, String tag, StringWithCustomTags valueToRightOfTag) throws GedcomWriterException {
        if (valueToRightOfTag == null || valueToRightOfTag.getValue() == null) {
            StringBuilder line = new StringBuilder(Integer.toString(level));
            line.append(" ").append(tag);
            baseWriter.lines.add(line.toString());
            if (valueToRightOfTag != null) {
                emitCustomTags(level + 1, valueToRightOfTag.getCustomTags());
            }
            return;
        }

        List<String> temp = new ArrayList<String>();
        temp.add(valueToRightOfTag.getValue());
        List<String> valueLines = splitLinesOnBreakingCharacters(temp);

        boolean first = true;
        for (String v : valueLines) {
            StringBuilder line = new StringBuilder();
            if (first) {
                line.append(level);
                line.append(" ").append(tag).append(" ").append(v);
                emitAndSplit(level, line.toString());
            } else {
                line.append(level + 1);
                line.append(" CONT ").append(v);
                emitAndSplit(level + 1, line.toString());
            }

            first = false;
        }
        emitCustomTags(level + 1, valueToRightOfTag.getCustomTags());
    }

    /**
     * Write a line and tag, with an optional value for the tag
     * 
     * @param level
     *            the level within the file hierarchy
     * @param tag
     *            the tag for the line of the file
     * @param value
     *            the value to write to the right of the tag
     * @throws GedcomWriterException
     *             if the value is null or blank
     */
    protected void emitTagWithRequiredValue(int level, String tag, String value) throws GedcomWriterException {
        emitTagWithRequiredValue(level, null, tag, new StringWithCustomTags(value));
    }

    /**
     * Write a line and tag, with an optional value for the tag
     * 
     * @param level
     *            the level within the file hierarchy
     * @param tag
     *            the tag for the line of the file
     * @param value
     *            the value to write to the right of the tag
     * @throws GedcomWriterException
     *             if the value is null or blank
     */
    protected void emitTagWithRequiredValue(int level, String tag, StringWithCustomTags value) throws GedcomWriterException {
        emitTagWithRequiredValue(level, null, tag, value);
    }

    /**
     * Returns true if and only if the Gedcom data says it is for the 5.5 standard.
     * 
     * @return true if and only if the Gedcom data says it is for the 5.5 standard.
     */
    protected boolean g55() {
        return baseWriter != null && baseWriter.writeFrom.getHeader() != null && baseWriter.writeFrom.getHeader().getGedcomVersion() != null
                && SupportedVersion.V5_5.equals(baseWriter.writeFrom.getHeader().getGedcomVersion().getVersionNumber());
    }

    /**
     * Write a line out to the print writer, splitting due to length if needed with CONC lines
     * 
     * @param level
     *            the level at which we are recording
     * @param line
     *            the line to be written, which may have line breaking characters (which will result in CONT lines)
     */
    void emitAndSplit(int level, String line) {
        if (line.length() <= MAX_LINE_LENGTH) {
            baseWriter.lines.add(line);
        } else {
            // First part
            baseWriter.lines.add(line.substring(0, MAX_LINE_LENGTH));
            // Now a series of as many CONC lines as needed
            String remainder = line.substring(MAX_LINE_LENGTH);
            while (remainder.length() > 0) {
                if (remainder.length() > MAX_LINE_LENGTH) {
                    baseWriter.lines.add(level + 1 + " CONC " + remainder.substring(0, MAX_LINE_LENGTH));
                    remainder = remainder.substring(MAX_LINE_LENGTH);
                } else {
                    baseWriter.lines.add(level + 1 + " CONC " + remainder);
                    remainder = "";
                }
            }
        }
    }

    /**
     * Emit the custom tags
     * 
     * @param customTags
     *            the custom tags
     * @param level
     *            the level at which the custom tags are to be written
     */
    void emitCustomTags(int level, List<StringTree> customTags) {
        if (customTags != null) {
            for (StringTree st : customTags) {
                StringBuilder line = new StringBuilder(Integer.toString(level));
                line.append(" ");
                if (st.getId() != null && st.getId().trim().length() > 0) {
                    line.append(st.getId()).append(" ");
                }
                line.append(st.getTag());
                if (st.getValue() != null && st.getValue().trim().length() > 0) {
                    line.append(" ").append(st.getValue());
                }
                baseWriter.lines.add(line.toString());
                emitCustomTags(level + 1, st.getChildren());
            }
        }
    }

    /**
     * Split up an array of text lines to when line break characters appear. If any of the original line contains line
     * split characters (newlines, line feeds, carriage returns), split the line up into multiple lines.
     * 
     * @param linesOfText
     *            a single string that may or may not contain line breaks
     * @return a list of Strings that reflect the line breaks in the original string
     */
    List<String> splitLinesOnBreakingCharacters(List<String> linesOfText) {
        List<String> result = new ArrayList<String>();
        if (linesOfText != null) {
            for (String s : linesOfText) {
                String[] pieces = s.split("(\r\n|\n\r|\r|\n)");
                for (String piece : pieces) {
                    result.add(piece);
                }
            }
        }
        return result;
    }

    /**
     * Write a line if the value is non-null
     * 
     * @param level
     *            the level within the file hierarchy
     * @param xref
     *            the xref for the item, if any
     * @param tag
     *            the tag for the line of the file
     * @param value
     *            the value to write to the right of the tag
     */
    private void emitTagIfValueNotNull(int level, String xref, String tag, Object value) {
        if (value != null) {

            List<String> temp = new ArrayList<String>();
            temp.add(value.toString());
            List<String> valueLines = splitLinesOnBreakingCharacters(temp);

            boolean first = true;
            for (String v : valueLines) {

                StringBuilder line = new StringBuilder();
                if (first) {
                    line.append(level);
                    if (xref != null && xref.length() > 0) {
                        line.append(" ").append(xref);
                    }
                    line.append(" ").append(tag).append(" ").append(v);
                    emitAndSplit(level, line.toString());
                } else {
                    line.append(level + 1);
                    line.append(" CONT ").append(v);
                    emitAndSplit(level + 1, line.toString());
                }

                first = false;
            }
        }
    }

    /**
     * Write a line and tag, with an optional value for the tag
     * 
     * @param level
     *            the level within the file hierarchy
     * @param tag
     *            the tag for the line of the file
     * @param e
     *            the value to write to the right of the tag
     * @param xref
     *            the xref of the item being emitted
     * @throws GedcomWriterException
     *             if the value is null or blank
     */
    private void emitTagWithRequiredValue(int level, String xref, String tag, StringWithCustomTags e) throws GedcomWriterException {
        if (e == null || e.getValue() == null || e.getValue().trim().length() == 0) {
            throw new GedcomWriterException("Required value for tag " + tag + " at level " + level + " was null or blank");
        }
        List<String> temp = new ArrayList<String>();
        temp.add(e.getValue());
        List<String> valueLines = splitLinesOnBreakingCharacters(temp);

        boolean first = true;
        for (String v : valueLines) {
            StringBuilder line = new StringBuilder();
            if (first) {
                line.append(level);
                if (xref != null && xref.length() > 0) {
                    line.append(" ").append(xref);
                }
                line.append(" ").append(tag).append(" ").append(v);
                emitAndSplit(level, line.toString());
            } else {
                line.append(level + 1);
                line.append(" CONT ").append(v);
                emitAndSplit(level + 1, line.toString());
            }

            first = false;
        }

        emitCustomTags(level + 1, e.getCustomTags());
    }
}
