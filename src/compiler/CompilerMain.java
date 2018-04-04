package compiler;

import parser.Parser;
import scanner.TokenType;
import syntaxtree.*;

/**
 * Main class to test reading in the file, parsing it, and then finally printing out a .symboltable file.
 * @author heechan
 *
 */
public class CompilerMain {

	public static void main(String[] args) {
		
		String filename = System.getProperty("user.dir") + "/" + "test.pas";					// This would mean a file test.pas needs to exist in the directory of the project.	
		Parser parser;
		
		try {		
			parser = new Parser(filename, true); 		// Initialize the parser using the constructor.
			ProgramNode pNode = parser.program();							// Call program() on the parser. This will use recursion to go through the pascal file.
			parser.writeOut(filename);					// This would print out a test.symboltable file to the directory of the project.
			System.out.println(pNode.indentedToString(0));
		} catch (Exception e) { e.printStackTrace(); }
		
		OperationNode oNode = new OperationNode(TokenType.MINUS);
		
		System.out.println(oNode.dataType);
		
	}
}
