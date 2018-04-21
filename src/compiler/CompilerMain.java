package compiler;

import codegeneration.CodeGeneration;
import parser.Parser;
import syntaxtree.*;
import semanticanalyzer.*;

/**
 * Main class to test reading in the file, parsing it, and then finally printing out a .symboltable file.
 * @author heechan
 *
 */
public class CompilerMain {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("No file.");
			System.exit(1);
		}
		
		String filename = args[0];
		
//		String filename = System.getProperty("user.dir") + "/" + "test.pas";					// This would mean a file test.pas needs to exist in the directory of the project.	
		Parser parser;
		
		try {		
			parser = new Parser(filename, true); 		// Initialize the parser using the constructor.
			ProgramNode pNode = parser.program();		// Call program() on the parser. This will use recursion to go through the pascal file.
			parser.writeOut(filename);					// This would print out a test.symboltable file to the directory of the project.
			
//			System.out.println(pNode.indentedToString(0));
			
			SemanticAnalyzer sA = new SemanticAnalyzer(pNode);
			sA.Analyze();
			pNode = sA.getPNode();
			
			sA.writeCodeToFile(filename);
//			System.out.println(sA.isFlagged());
//			System.out.println(pNode.indentedToString(0));
			
			if (sA.isFlagged()) {
				System.out.println("There are error(s) in the code, thus code generation will not occur.");
			} else {
				CodeGeneration.writeCodeToFile(filename, pNode);
			}
		} catch (Exception e) { e.printStackTrace(); }	
	}
}
