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
	void testProgramNodeA() {
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
	
	/**
	 * Creating a ProgramNode manually and compare it to the one Parser creates.
	 * Different than the first one, here we add additional variables, but of course, as it hasn't been implemented,
	 * this test should pass as well.
	 */
	@Test
	void testProgramNodeB() {
		ProgramNode pNode = new ProgramNode("foo");
		pNode.setVariables(new DeclarationsNode());
		pNode.setFunctions(new SubProgramDeclarationsNode());
		pNode.setMain(new CompoundStatementNode());
		
		String expected = pNode.indentedToString(0);
		
		String test = "program foo; var num, f : integer; begin end .";
		Parser p = new Parser(test, false);
		
		String actual = p.program().indentedToString(0);
		
		assertEquals(expected, actual);
	}
}
