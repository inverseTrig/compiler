package parser;

import static org.junit.jupiter.api.Assertions.*;

import syntaxtree.*;
import org.junit.jupiter.api.Test;

/**
 * JUnit testing for SyntaxTree integration to the Parser.
 * @author heechan
 *
 */
class SyntaxTreeTest {

	/**
	 * Creating a ProgramNode manually and compare it to the one Parser creates.
	 */
	@Test
	void testProgramNode() {
		ProgramNode pNode = new ProgramNode("foo");
		pNode.setVariables(new DeclarationsNode());
		pNode.setFunctions(new SubProgramDeclarationsNode());
		pNode.setMain(new CompoundStatementNode());
		
		String expected = pNode.indentedToString(0);
		
		String test = "program foo; begin end .";
		Parser p = new Parser(test, false);
		
		String actual = p.program().indentedToString(0);
		
		assertEquals(expected, actual);
	}
	
	
}
