package parser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ParserTest {

	//program
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
	
	//declarations
	@Test
	void testDeclarations() {
		System.out.println("\n------Testing declarations():");

		String test = "var foo : real ; var fi: integer;";
		System.out.println("---with \"" + test + "\" NOT supposed to have errors");
		Parser parser = new Parser(test, false);
		parser.declarations();

		test = "var foo : fum ;";
		System.out.println("---with \"" + test + "\" supposed to have errors");
		parser = new Parser(test, false);
		parser.declarations();

		test = "var ;";
		System.out.println("---with \"" + test + "\" supposed to have errors");
		parser = new Parser(test, false);
		parser.declarations();
	}
	
	//subprogram_declaration
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
	
	//statement
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
	
	//factor
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
	
	
	//simple_expression
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
