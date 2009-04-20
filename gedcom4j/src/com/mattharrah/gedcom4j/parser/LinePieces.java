/*
 * Copyright (c) 2009 Matthew R. Harrah
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
package com.mattharrah.gedcom4j.parser;

public class LinePieces {
	public int level;
	public String id;
	public String tag;
	public String remainder;

	public LinePieces(String line) {
		String[] parts = line.split(" ");
		level = Integer.parseInt(parts[0]);
		if (parts.length == 2) {
			tag = parts[1];
		} else if (parts.length >= 3) {
			if (parts[1].startsWith("@") && parts[1].endsWith("@")) {
				tag = parts[2];
				id = parts[1];
				remainder = joinParts(parts, 3);
			} else {
				tag = parts[1];
				remainder = joinParts(parts, 2);
			}
		}
	}

	private String joinParts(String[] parts, int startFrom) {
		StringBuilder sb = new StringBuilder();
		for (int i = startFrom; i < parts.length; i++) {
			sb.append(parts[i]);
			if (i < parts.length - 1) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}
}
