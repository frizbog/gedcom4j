package org.gedcom4j.io;

/**
 * <p>
 * This is a helper class that deals with diacritical marks for ANSEL.
 * </p>
 * <p>
 * In ANSEL, diacriticals are separate characters from the ones they modify/decorate. The base character is preceded by
 * zero, one, or two diacritical characters. These diacriticals are in the 0xE0 to 0xFE range, and when rendered do not
 * take up any horizontal space -- that being left to the base character being decorated.
 * </p>
 * <p>
 * In Java strings, which are UTF-16 (which, in the characters we are using, is essentially the same as unicode), these
 * diacritical marks, when used, come <em>after</em> the character being modified. This means that ANSEL order and Java
 * order are reversed, so something needs to be done.
 * </p>
 * <p>
 * The solution selected here, however, is not just to change the order of the bytes, but to combine the bytes, whenever
 * possible, into pre-combined unicode characters when reading, and to reverse the process for writing.
 * </p>
 * <p>
 * For example - consider the lowercase a with an acute accent: <tt>&#x00E1;</tt>.
 * </p>
 * <ul>
 * <li>ANSEL should render this as 0xE2 0x61. 0xE2 is the acute accent, and 0x61 is the lowercase a. The two characters
 * combine when rendered to look like the desired glyph, but is actually two characters that overlap.</li>
 * <li>UTF-16 (i.e., a Java string) could/should render this as 0x0061 0x0301. 0x0061 is the lowercase a, and 0x0301 is
 * the unicode version of the combining acute accent. The two characters combine as in the ANSEL scenario.</li>
 * <li>UTF-16 ideally would render this as the 0x00E1 character, which is a combined lowercase a with the acute accent
 * already part of the glyph. This is now a single character in the Java string to represent the desired glyph.</li>
 * </ul>
 * 
 * @author frizbog
 */
class AnselDiacriticalHandler {

}
