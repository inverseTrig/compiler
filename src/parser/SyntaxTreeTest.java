package parser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SyntaxTreeTest {

	@Test
	void test() {
		String expected = "Program: test\r\n" + 
				"|-- Declarations\r\n" + 
				"|-- SubProgramDeclarations\r\n" + 
				"|-- Compound Statement";
		
		String test = "program test; begin end .";
		Parser p = new Parser(test, false);
		
		String actual = p.program().indentedToString(0);
		
		expected = expected.replaceAll("\n", "").replaceAll("\r", "");
		actual = actual.replaceAll("\n", "").replaceAll("\r", "");
		
		assertEquals(expected, actual);
	}
}
