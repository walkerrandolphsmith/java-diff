package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DiffCounter {
	public ArrayList<String> deletedLines = new ArrayList<String>();
	public ArrayList<String> addedLines = new ArrayList<String>();
	public String file1, file2;
	public int LOCadded, LOCdeleted, LOCmodified;
	public int originalCount;
	public int newCount;
	public String originalFile1;
	public String originalFile2;

	public DiffCounter(File one, File two, int o, int n) throws FileNotFoundException {
		
		originalCount = o;
		newCount = n;
		// Convert File One to String
		StringBuilder sb = new StringBuilder();
		StringBuilder formatedSB = new StringBuilder();
		Scanner scan = new Scanner(one);

		while (scan.hasNextLine()) {
			String s = scan.nextLine();
			sb.append(s);
			sb.append('\n');
				
			formatedSB.append("<li>" + s + "</li>");
			formatedSB.append("<br>");
		}
		this.file1 = sb.toString();
		originalFile1 = formatedSB.toString();
		scan.close();

		// Convert File Two to String
		sb = new StringBuilder();
		formatedSB = new StringBuilder();
		scan = new Scanner(two);

		while (scan.hasNextLine()) {
			String s = scan.nextLine();
			sb.append(s);
			sb.append('\n');
			
			formatedSB.append("<li>" + s + "</li>");
			formatedSB.append("<br>");
		}
		scan.close();
		file2 = sb.toString();
		originalFile2 = formatedSB.toString();

		file1 = stripWithRegex(file1);
		file2 = stripWithRegex(file2);

		LOCadded = 0;
		LOCdeleted = 0;

		setup();
	}

	public String stripWithRegex(String f) {
		f = f.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
		f = f.replaceAll("\\{", "lbracket");
		f = f.replaceAll("\\}", "rbracket");
		f = f.replaceAll("\\[", "");
		f = f.replaceAll("\\]", "");
		f = f.replaceAll("\\(", "");
		f = f.replaceAll("\\)", "");
		f = f.replaceAll("[\r\t]", "");
		f = f.replaceAll("<|>|=|%|\\||\\&", "");
		f = f.replaceAll("\\+|\\-|\\*|\\/", "");
		f = f.replaceAll(".*\\bimport\\b.*", "");
		f = f.replaceAll(".*\\bpackage\\b.*", "");
		return f;
	}

	public boolean isModified(String line1, String remaingLine)
			throws FileNotFoundException {
		boolean result = false;
		int count = 0;
		char[] r = remaingLine.toCharArray();
		ArrayList<Character> charsInR = new ArrayList<Character>();

		for (int i = 0; i < r.length; i++) {
			charsInR.add(r[i]);
		}

		for (int i = 0; i < line1.length(); i++) {
			for (int j = 0; j < charsInR.size(); j++) {
				if (line1.charAt(i) == charsInR.get(j)) {
					count++;
					charsInR.remove(j);
				}
			}
		}

		double amount = (double) count / (double) line1.length();
		if (amount > 0.9) {
			result = true;
		}
		return result;
	}


	public void setup() throws FileNotFoundException {
		if (file1.length() == 0) {
			LOCadded = file2.length();
		} else if (file2.length() == 0) {
			LOCdeleted = file1.length();
		} else {
			Scanner scan1 = new Scanner(file1);
			Scanner scan2 = new Scanner(file2);
			while (scan1.hasNextLine()) {
				String file1_line = scan1.nextLine();
				if (!file1_line.equals("")) {
					boolean isFound = false;
					scan2 = new Scanner(file2);
					while (scan2.hasNextLine() && !isFound) {
						String file2_line = scan2.nextLine();
						if (file1_line.equals(file2_line)) {
							file2 = file2.replaceFirst(file1_line, "");
							isFound = true;
						}
					}
					if (!isFound) {
						deletedLines.add(file1_line);
					}
				}
			}
			scan1.close();
			scan2.close();

			Scanner scanRemaining = new Scanner(file2);
			while (scanRemaining.hasNextLine()) {
				String line = scanRemaining.nextLine();
				if (!line.equals(""))
					addedLines.add(line);
			}
			scanRemaining.close();
			
			ArrayList<String> newDeleted = deletedLines;			
			ArrayList<String> newAdded = addedLines;

			for (int i = 0; i < deletedLines.size(); i++) {
				for (int j = 0; j < addedLines.size(); j++) {
					if(i < deletedLines.size() && j < addedLines.size()){
					String deletedLineOfCode = deletedLines.get(i);
					String addedLineOfCode = addedLines.get(j);
						if (isModified(deletedLineOfCode, addedLineOfCode)) {
							newDeleted.remove(0);
							newAdded.remove(0);
							LOCmodified++;
							i++;
						}
					}
				}
			}
			for (int i = 0; i < newAdded.size(); i++) {
				LOCadded++;
			}
			for (int i = 0; i < newDeleted.size(); i++) {
				LOCdeleted++;
			}
		}
	}

}