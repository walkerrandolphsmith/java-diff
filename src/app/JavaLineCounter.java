package app;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import util.FileChooser;
import util.antlr.JavaLexer;
import util.antlr.JavaParser;

/**
 * JavaLineCounter computes the line count of a .&nbsp;java file.
 * <strong>Ignoring</strong>
 * <ul>
 * <li>White space</li>
 * <li>Blank Lines</li>
 * <li>Package Declarations</li>
 * <li>Import Statements</li>
 * <li>Single Line Comments</li>
 * <li>Block Comments</li>
 * </ul>
 * 
 * 
 * @author Walker Randolph Smith
 * @version 1.0
 */

public class JavaLineCounter {
	
	public static ArrayList<ArrayList<ParseTree>> files = new ArrayList<ArrayList<ParseTree>>();
	public static ArrayList<ParseTree> nodes = new ArrayList<ParseTree>();

	public static File f;
	public static int LOC = 0;

	/**
	 * 
	 * Prints the line count of a .&nbsp;java file to the console. Main method
	 * and entry point to the application.
	 * 
	 * @since 1.0
	 * 
	 */

	public static void main(String[] args) throws Exception {
		
		FileChooser window = new FileChooser();
		f = window.getFile();
		window.setVisible(true);
		

		FileInputStream fStream = new FileInputStream(f);
		ANTLRInputStream input = new ANTLRInputStream(fStream);

		JavaLexer lexer = new JavaLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		JavaParser parser = new JavaParser(tokens);
		ParseTree tree = parser.compilationUnit();

		for (int i = 0; i < tokens.size(); i++) {
			int c = tokens.get(i).getType();
			switch (c) {
			case 6:
				LOC += 1;
				break;
			case 63:
				LOC += 1;
				break;

			case 9:
				LOC += 2;
				break;

			case 28:
				LOC += 2;
				break;

			case 21:
				LOC += 0;
				break;

			case 22:
				LOC += 2;
				break;

			case 41:
				LOC += 2;
				break;

			case 50:
				LOC += 2;
				break;

			case 47:  
				LOC += 4;
				break;

			case 25:
				LOC -= 1;
				break;

			case 32:
				LOC -= 1;
				break;
			}
		}

		input = new ANTLRInputStream(tree.toStringTree(parser));

		lexer = new JavaLexer(input);
		tokens = new CommonTokenStream(lexer);
		parser = new JavaParser(tokens);
		tree = parser.compilationUnit();

		visit(tree);
		window.dispose();
	}

	/**
	 * Traverses an Abstract Syntax Tree. An ANTLR parser can be leveraged to
	 * create an AST. Traverse the AST in order to count the method
	 * declarations.
	 * 
	 * @param tree
	 *            the AST produced by an ANTLR parser
	 *            
	 */

	public static void visit(ParseTree tree) {
		int length = tree.getChildCount();	
		for (int i = 0; i < length; i++) {
			ParseTree child = tree.getChild(i);
			nodes.add(child);
			if ("methodDeclaration".compareTo(child.getText()) == 0) {
				LOC += 2;
			}
			if ("constructorDeclaration".compareTo(child.getText()) == 0) {
				LOC += 2;
			}
			visit(child);
		}
	}
}
