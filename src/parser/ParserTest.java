package parser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ParserTest {

	//program
	@Test
	void testProgram() {
		System.out.println("-----Testing program(): This isn't supposed to have any errors.");
		Parser parser = new Parser("program foo ; begin end .", false);
		parser.program();
		
		System.out.println("\n-----Testing program(): This is supposed to have errors.");
		parser = new Parser("program foo : begin end .", false);
		parser.program();
		
		System.out.println("\n-----Testing program(): This is supposed to have errors.");
		parser = new Parser("program foo : begin end .", false);
		parser.program();
	}
	//declarations
	
	//subprogram_declaration
	
	//statement
	
	//simple_expression
	@Test
	void testSimpleExpression() {
		System.out.println("\n-----Testing simple_expression(): This isn't supposed to have any errors.");
		Parser parser = new Parser("foo * 3 + fi", false);
		parser.simple_expression();
		
		System.out.println("\n-----Testing simple_expression(): This is supposed to have errors.");
		parser = new Parser("326 + -", false);
		parser.simple_expression();
		
		System.out.println("\n-----Testing simple_expression(): This is supposed to have errors.");
		parser = new Parser("+", false);
		parser.simple_expression();
	}
	
	//factor
	@Test
	void testFactor() {
		System.out.println("\n-----Testing factor(): This isn't supposed to have any errors.");
		Parser parser = new Parser("45 :", false);
		parser.factor();
		
		System.out.println("\n-----Testing factor(): This is supposed to have errors.");
		parser = new Parser("()", false);
		parser.factor();
		
		System.out.println("\n-----Testing factor(): This isn't supposed to have any errors.");
		parser = new Parser("45 :", false);
		parser.factor();
	}
	
}
