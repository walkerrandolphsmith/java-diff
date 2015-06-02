package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import util.FileChooser;

public class Diff {

	public String file1, file2;
	public int aCount, dCount;
	
	public Diff(File one, File two) throws FileNotFoundException{
		
		//Convert File One to String
		StringBuilder sb = new StringBuilder();
		Scanner scan = new Scanner(one);	
		
		while(scan.hasNextLine()){
			sb.append(scan.nextLine());
			sb.append('\n');
		}
		this.file1 = sb.toString();

		
		//Convert File Two to String
		sb = new StringBuilder();
		scan = new Scanner(two);
		
		while(scan.hasNextLine()){
			sb.append(scan.nextLine());
			sb.append('\n');
		}
		file2 = sb.toString();
		
		
		
		file1 = file1.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)","");//Remove comment
		file1 = file1.replaceAll("\\{","");
		file1 = file1.replaceAll("\\}","");
		file1 = file1.replaceAll("\\[","");
		file1 = file1.replaceAll("\\]","");
		


		file2 = file2.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)","");
		file2 = file2.replaceAll("\\{","");
		file2 = file2.replaceAll("\\}","");
		file2 = file2.replaceAll("\\[","");
		file2 = file2.replaceAll("\\]","");

		aCount = 0;
		dCount = 0;
		
		setup();
	}
	
	public void printResult(){
		System.out.println("Added lines: " + aCount + " \nDeleted lines: " + dCount);
	}
	
	public static void main (String [] args) throws FileNotFoundException{
		FileChooser window = new FileChooser();
		File file1 = window.getFile();
				
		window = new FileChooser();
		File file2 = window.getFile();
		window.setVisible(true);
		window.dispose();
		
		Diff diff = new Diff(file1, file2);
		diff.printResult();
	}
	
	public void setup(){
		
		if(file1.length() == 0){
			aCount = file2.length();
		}else if(file2.length() == 0){
			dCount = file1.length();
		}else{		
			Scanner scan1 = new Scanner(file1);
			Scanner scan2 = new Scanner(file2);
			while(scan1.hasNextLine()){
				String file1_line = scan1.nextLine();
				
					boolean isFound = false;
					while(scan2.hasNextLine() && !isFound){
						String file2_line = scan2.nextLine();
						if(file1_line.equals(file2_line)){
							System.out.println(file1_line);
							file2 = file2.replaceFirst(file1_line, "");
							isFound = true;
						}
					}
					if(!isFound){
						dCount += 1;
					}
			}
			
			scan2 = new Scanner(file2);
			while(scan2.hasNextLine()){
				System.out.println(scan2.nextLine());
			}
			
			
		}
	}
	

	
}