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

/**
 * A family record. Corrsponds to FAM_RECORD in the GEDCOM spec.
 * 
 * @author frizbog1
 */
public class Family {
	/**
	 * The permanent record file number
	 */
	public String recFileNumber;
	/**
	 * The automated record ID number
	 */
	public String automatedRecordId;
	/**
	 * The wife in the family
	 */
	public Individual wife;
	/**
	 * The husband in the family
	 */
	public Individual husband;
	/**
	 * A list of the children in the family
	 */
	public List<Individual> children = new ArrayList<Individual>();
	/**
	 * The number of children
	 */
	public Integer numChildren;
	/**
	 * A list of the submitters for this family
	 */
	public List<Submitter> submitters = new ArrayList<Submitter>();
	/**
	 * The LDS Spouse Sealings for this family
	 */
	public List<LdsFamilyOrdinance> ldsSpouseSealings = new ArrayList<LdsFamilyOrdinance>();
	/**
	 * The source citations for this family
	 */
	public List<Citation> citations = new ArrayList<Citation>();
	/**
	 * The multimedia for this family
	 */
	public List<Multimedia> multimedia = new ArrayList<Multimedia>();
	/**
	 * The change date information for this family record
	 */
	public ChangeDate changeDate;
	/**
	 * All the family events
	 */
	public List<FamilyEvent> events = new ArrayList<FamilyEvent>();
	/**
	 * Notes on this family
	 */
	public List<Note> notes = new ArrayList<Note>();
	/**
	 * A cross reference id that things can use to identify this family
	 */
	public String xref;
	/**
	 * The user references
	 */
	public List<UserReference> userReferences = new ArrayList<UserReference>();
}
