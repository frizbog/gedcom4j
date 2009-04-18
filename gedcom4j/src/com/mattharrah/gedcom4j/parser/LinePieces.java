package com.mattharrah.gedcom4j.parser;

public class LinePieces {
	public int level;
	public String id;
	public String tag;
	public String remainder;
	
	public LinePieces(String line) {
		String[] parts = line.split(" ");
		level = Integer.parseInt(parts[0]);
		if (parts.length==2) {
			tag = parts[1];
		} else if (parts.length>=3) {
			if (parts[1].startsWith("@") && parts[1].endsWith("@")) {
				tag = parts[2];
				id = parts[1];
				remainder = joinParts(parts,3);
			} else {
				tag = parts[1];
				remainder = joinParts(parts,2);
			}
		}
	}

	private String joinParts(String[] parts, int startFrom) {
		StringBuilder sb = new StringBuilder();
		for (int i = startFrom; i < parts.length; i++) {
			sb.append(parts[i]);
			if (i < parts.length-1) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}
}
