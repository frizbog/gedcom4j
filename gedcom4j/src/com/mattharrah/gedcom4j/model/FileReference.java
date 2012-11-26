/*
 * Copyright (c) 2009-2012 Matthew R. Harrah
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

package com.mattharrah.gedcom4j.model;

/**
 * A file referred to in a Multimedia record or link
 * 
 * @author frizbog
 * 
 */
public class FileReference {

    /**
     * The actual reference to the file - a URL, a file name, something
     */
    public String referenceToFile;

    /**
     * The format of the referenced file
     */
    public String format;

    /**
     * The media type of the referenced file
     */
    public String mediaType;

    /**
     * The descriptive title for this file reference
     */
    public String title;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FileReference other = (FileReference) obj;
        if (format == null) {
            if (other.format != null) {
                return false;
            }
        } else if (!format.equals(other.format)) {
            return false;
        }
        if (mediaType == null) {
            if (other.mediaType != null) {
                return false;
            }
        } else if (!mediaType.equals(other.mediaType)) {
            return false;
        }
        if (referenceToFile == null) {
            if (other.referenceToFile != null) {
                return false;
            }
        } else if (!referenceToFile.equals(other.referenceToFile)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (format == null ? 0 : format.hashCode());
        result = prime * result + (mediaType == null ? 0 : mediaType.hashCode());
        result = prime * result + (referenceToFile == null ? 0 : referenceToFile.hashCode());
        result = prime * result + (title == null ? 0 : title.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "FileReference [referenceToFile=" + referenceToFile + ", format=" + format + ", mediaType=" + mediaType + ", title=" + title + "]";
    }

}
