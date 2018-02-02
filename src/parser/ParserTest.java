package parser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ParserTest {

	//program
	
	//declarations
	
	//subprogram_declaration
	
	//statement
	
	//simple_expression
	
	//factor
	@Test
	void testFactor() {
		Parser parser = new Parser("45 ;", false);
		parser.factor();
	}
}
