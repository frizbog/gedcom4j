package org.gedcom4j.io;

/**
 * The types of line terminators that are supported by the GedcomFileWriter
 * 
 * @author frizbog1
 */
enum LineTerminator {
    /**
     * CR only - used mostly by old Macs
     */
    CR_ONLY,
    /**
     * CR + LF - used mostly for Windows
     */
    CRLF,
    /**
     * LF only - used mostly by Linux and newer Macs
     */
    LF_ONLY,
    /**
     * LF+CR - not commonly used, but GEDCOM supports it
     */
    LFCR
}
