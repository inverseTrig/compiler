package parser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import symboltable.Kind;
import symboltable.SymbolTable;

/**
 * JUnit test class to test SymbolTable integration to the Parser.
 * @author heechan
 *
 */
class SymbolTableIntegrationTest {

	// Create string to use in our testing.
	String var = "fum := 3 - 2";
	String proc = "foo (3)";
	
	/**
	 * Here, there should be no errors thrown. This is because String var matches perfectly with variable() assignop expression()
	 */
	@Test
	void testStatementA() {
		Parser parserA = new Parser(var, false);
		parserA.getSymbolTable().add("fum", Kind.VARIABLE);
		parserA.statement();
		System.out.println(parserA.getSymbolTable());
		System.out.println("Yay! No errors!\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
	}

	/**
	 * Here, again, there should be no errors thrown. This is because our procedure_statement() matches ID, then
	 * if and only if the next is a left parentheses, does it match the left parentheses.
	 * As the next lexeme it sees is assignop, it just doesn't do anything after ID.
	 */
	@Test
	void testStatementB() {
		Parser parserB = new Parser(var, false);
		parserB.getSymbolTable().add("fum", Kind.PROCEDURE);
		System.out.println(parserB.getSymbolTable());
		parserB.statement();
		System.out.println("Yay! No errors again!\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
	}
	
	/**
	 * Here, this should throw an error because it tries to match assignop with a left parentheses.
	 */
	@Test
	void testStatementC() {
		Parser parserC = new Parser(proc, false);
		parserC.getSymbolTable().add("foo", Kind.VARIABLE);
		System.out.println(parserC.getSymbolTable());
		parserC.statement();
		System.out.println("Yay! Throws an error!\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
	}
	
	/**
	 * Here, there should be no errors because it matches the format of procedure_statement() perfectly.
	 * Matching ( expression_list() )
	 */
	@Test
	void testStatementD() {
		Parser parserD = new Parser(proc, false);
		parserD.getSymbolTable().add("foo", Kind.PROCEDURE);
		System.out.println(parserD.getSymbolTable());
		parserD.statement();
		System.out.println("Yay! No errors here either!\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
	}
}
