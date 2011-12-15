/*
 * Copyright (c) 2009-2011 Matthew R. Harrah
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
package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

public class RepositoryCitation {

	public Repository repository;
	public List<Note> notes = new ArrayList<Note>();
	public List<SourceCallNumber> callNumbers = new ArrayList<SourceCallNumber>();

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
		RepositoryCitation other = (RepositoryCitation) obj;
		if (callNumbers == null) {
			if (other.callNumbers != null) {
				return false;
			}
		} else if (!callNumbers.equals(other.callNumbers)) {
			return false;
		}
		if (notes == null) {
			if (other.notes != null) {
				return false;
			}
		} else if (!notes.equals(other.notes)) {
			return false;
		}
		if (repository == null) {
			if (other.repository != null) {
				return false;
			}
		} else if (!repository.equals(other.repository)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
		        + ((callNumbers == null) ? 0 : callNumbers.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result
		        + ((repository == null) ? 0 : repository.hashCode());
		return result;
	}

}
