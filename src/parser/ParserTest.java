package parser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import syntaxtree.DeclarationsNode;

class ParserTest {

	/**
	 * Testing program()
	 * Not all tests were meant to work; the error will be shown via the error() method.
	 * For the first case, there should be no error
	 * For the second case, by having a COLON instead of SEMICOLON, an error should be displayed.
	 * For the third case, having a typo for PROGRAM, an error should be displayed.
	 */
	@Test
	void testProgram() {
		System.out.println("\n------Testing program():");

		String test = "program foo ; begin end .";
		System.out.println("---with \"" + test + "\" NOT supposed to have errors");
		Parser parser = new Parser(test, false);
		parser.program();

		test = "program foo : begin end .";
		System.out.println("---with \"" + test + "\" supposed to have errors");
		parser = new Parser(test, false);
		parser.program();

		test = "progrm ; begin end .";
		System.out.println("---with \"" + test + "\" supposed to have errors");
		parser = new Parser(test, false);
		parser.program();
	}
	
	/**
	 * Testing declarations()
	 * Not all tests were meant to work; the error will be shown via the error() method.
	 * For the first case, there should be no error.
	 * For the second case, by having an ID instead of a type, an error should be displayed.
	 * For the third case, by having a SEMICOLON instead of an ID, an error should be displayed.
	 */
	@Test
	void testDeclarations() {
		System.out.println("\n------Testing declarations():");

		String test = "var foo : real ; var fi: integer;";
		System.out.println("---with \"" + test + "\" NOT supposed to have errors");
		Parser parser = new Parser(test, false);
		parser.declarations(new DeclarationsNode());

		test = "var foo : fum ;";
		System.out.println("---with \"" + test + "\" supposed to have errors");
		parser = new Parser(test, false);
		parser.declarations(new DeclarationsNode());

		test = "var ;";
		System.out.println("---with \"" + test + "\" supposed to have errors");
		parser = new Parser(test, false);
		parser.declarations(new DeclarationsNode());
	}
	
	/**
	 * Testing subprogram_declaration()
	 * Not all tests were meant to work; the error will be shown via the error() method.
	 * For the first case, there should be no errors.
	 * For the second case, as we have VAR instead of PROCEDURE or FUNCTION, an error is displayed.
	 * For the third case, as we don't have a BEGIN, an error should be displayed.
	 */
	@Test
	void testSubprogramDeclaration() {
		System.out.println("\n------Testing subprogram_declaration():");

		String test = "procedure fum ; var foo : integer ; begin end";
		System.out.println("---with \"" + test + "\" NOT supposed to have errors");
		Parser parser = new Parser(test, false);
		parser.subprogram_declaration();

		test = "var foo : integer ;";
		System.out.println("---with \"" + test + "\" supposed to have errors");
		parser = new Parser(test, false);
		parser.subprogram_declaration();

		test = "function fo (fum : integer) : real ;";
		System.out.println("---with \"" + test + "\" supposed to have errors");
		parser = new Parser(test, false);
		parser.subprogram_declaration();
	}
	
	/**
	 * Testing statement()
	 * Not all tests were meant to work; the error will be shown via the error() method.
	 * For the first case, there should be no errors.
	 * For the second case, there should be no errors.
	 * For the third case, as we have an EQUAL sign rather than an ASSIGN, an error should be displayed.
	 */
	@Test
	void testStatement() {
		System.out.println("\n------Testing statement():");

		String test = "foo := -fum * 3";
		System.out.println("---with \"" + test + "\" NOT supposed to have errors");
		Parser parser = new Parser(test, false);
		parser.statement();

		test = "if foo + 3 < 6 then foo := 5 else foo := 7";
		System.out.println("---with \"" + test + "\" NOT supposed to have errors");
		parser = new Parser(test, false);
		parser.statement();

		test = "while foo do fum = fum + 1";
		System.out.println("---with \"" + test + "\" supposed to have errors");
		parser = new Parser(test, false);
		parser.statement();
	}
	
	/**
	 * Testing factor()
	 * Not all tests were meant to work; the error will be shown via the error() method.
	 * For the first case, there should be no error displayed.
	 * For the second case, as we have no expression within the parentheses, an error should be displayed.
	 * For the third case, as we have a right square bracket instead of a right parentheses, an error should be displayed.
	 */
	@Test
	void testFactor() {
		System.out.println("\n------Testing factor():");

		String test = "45";
		System.out.println("---with \"" + test + "\" NOT supposed to have errors");
		Parser parser = new Parser(test, false);
		parser.factor();

		test = "()";
		System.out.println("---with \"" + test + "\" supposed to have errors");
		parser = new Parser(test, false);
		parser.factor();

		test = "foo ( 2 ]";
		System.out.println("---with \"" + test + "\" supposed to have errors");
		parser = new Parser(test, false);
		parser.factor();
	}
	
	
	/**
	 * Testing simple_expression()
	 * Not all tests were meant to work; the error will be shown via the error() method.
	 * For the first case, there should be no error displayed.
	 * For the second case, as we have + followed by a -, an error should be displayed.
	 * For the third case, as we only have a +, an error should be displayed.
	 */
	@Test
	void testSimpleExpression() {
		
		System.out.println("\n------Testing simple_expression():");

		String test = "foo * 3 + fi";
		System.out.println("---with \"" + test + "\" NOT supposed to have errors");
		Parser parser = new Parser(test, false);
		parser.simple_expression();

		test = "326 + -";
		System.out.println("---with \"" + test + "\" supposed to have errors");
		parser = new Parser(test, false);
		parser.simple_expression();

		test = "+";
		System.out.println("---with \"" + test + "\" supposed to have errors");
		parser = new Parser(test, false);
		parser.simple_expression();
	}
	

	
}
